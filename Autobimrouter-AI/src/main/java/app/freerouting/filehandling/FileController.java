package app.freerouting.filehandling;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Locale;

import app.freerouting.board.BoardObservers;
import app.freerouting.board.ItemIdNoGenerator;
import app.freerouting.designforms.specctra.DsnFile;
import app.freerouting.gui.DesignFile;
import app.freerouting.gui.FileFormat;
import app.freerouting.guinew.App;
import app.freerouting.guinew.MainFrame;
import app.freerouting.interactive.BoardHandling;
import app.freerouting.logger.FRLogger;
import app.freerouting.settings.GlobalSettings;


/**
 * Controller component to handle file input/output upon
 * user input.
 *
 * @author Austin Kwon
 * @since 2024-10-21
 */
public class FileController {
    private File fileInput = null;
    private File fileOutput = null;

    private DesignFile fileDesign = null;

    public BoardHandling boardHandling = null;
    private BoardObservers boardObservers;
    MainFrame frame;

    /**
     * Default constructor that takes in a main frame.
     *
     * @param frame main frame
     */
    public FileController(MainFrame frame) {
        System.out.println("FileController constructor called!");
        this.frame = frame;
        boardHandling = new BoardHandling(this.frame, Locale.getDefault(), false, 0.01f);
    }

    /**
     * Constructor that takes in a main frame and an existing board handler.
     *
     * @param frame main frame
     * @param p_board_handling  existing board handler
     */
    public FileController(MainFrame frame, BoardHandling p_board_handling) {
        System.out.println("FileController(BoardHandling) constructor called!");
        this.frame = frame;
        this.boardHandling = p_board_handling;

        if (boardHandling != null) {
            System.out.println("New board handling assigned!");
        }
    }

    /**
     * Constructor that takes in a main frame and an existing board observers.
     *
     * @param frame main frame
     * @param p_board_observers  board observers
     */
    public FileController(MainFrame frame, BoardObservers p_board_observers) {
        System.out.println("FileController constructor(p_board_observers) called!");
        this.frame = frame;
        this.boardObservers = p_board_observers;
        boardHandling = new BoardHandling(this.frame, Locale.getDefault(), false, 0.01f);
    }

    public void setInputFile(File inputFile) {
        System.out.println("setInputFile() called!");
        this.fileInput = inputFile;
    }

    public void setOutputFile(File outputFile) {
        System.out.println("setOutputFile() called!");
        this.fileOutput = outputFile;
    }

    public void setDesignFile(File p_design_file) {
        System.out.println("setDesignFile() called!");
        this.fileDesign = new DesignFile(p_design_file);
    }

    public File getInputFile() {
        System.out.println("getInputFile() called!");
        return fileInput;
    }

    public File getOutputFile() {
        System.out.println("getOutputFile() called!");
        return fileOutput;
    }

    public DesignFile getDesignFile() {
        return fileDesign;
    }

    public String getDesignFileName() {
        System.out.println("getDesignFileName() called!");
        return fileDesign.get_name();
    }

    public void resetBoardHandler() {
        System.out.println("resetBoardHandler() called!");
        boardHandling = new BoardHandling(this.frame, Locale.getDefault(), false, 0.01f);
        if (boardHandling != null) {
            System.out.println("New board handling assigned!");
        }
    }

    public BoardHandling getBoardHandler() {
        return boardHandling;
    }

    public void updateBoardHandlerSettings() {
        System.out.println("Updating Board Handler settings...");
        boardHandling.settings.autoroute_settings.set_stop_pass_no(
            boardHandling.settings.autoroute_settings.get_start_pass_no() + frame.max_passes - 1);
        boardHandling.set_num_threads(frame.num_threads);
        boardHandling.set_board_update_strategy(frame.board_update_strategy);
        boardHandling.set_hybrid_ratio(frame.hybrid_ratio);
        boardHandling.set_item_selection_strategy(frame.item_selection_strategy);
    }

    public void load() {
        InputStream inputStream = null;
        DsnFile.ReadResult read_result = null;

        this.resetBoardHandler();

        if ((fileDesign != null) && fileDesign.inputFileFormat.equals(FileFormat.DSN)) {
            System.out.println("Design file loaded!");
        }

        if (fileDesign.get_input_stream() != null) {
            System.out.println("Design file input stream found!");
        }
        else {
            System.out.println("Design file input stream is NULL");
        }

        inputStream = fileDesign.get_input_stream();

        if (inputStream != null) {
            System.out.println("inputStream has the design file input stream!");
        }
        else {
            System.out.println("inputStream is NULL");
        }

        read_result = boardHandling.loadFromSpecctraDsn(inputStream, boardObservers, new ItemIdNoGenerator());

        if ((read_result != null) && (read_result == DsnFile.ReadResult.OK)) {
            System.out.println("DSN file successfully read!");
        }
        else {
            System.out.println("read_result is NULL!");
        }

        try {
            if (boardHandling.get_routing_board() != null) {
                System.out.println("Routing board loaded!");
            }
        } catch (Exception e) {
            System.out.println("Routing board NOT loaded!");
        }

        App.globalSettings.input_directory = getDesignFile().getInputFileDirectory();
        frame.setFileDirectory(getDesignFile().getInputFileDirectory());

        try {
          GlobalSettings.save(App.globalSettings);
          System.out.println("Directory saved!");
        } catch (Exception e) {
          // it's ok if we can't save the configuration file
          FRLogger.error("Couldn't update the input directory in the configuration file", e);
        }

    }

    public boolean writeOutputFile(File p_file_output) {
        String path;
        this.fileOutput = p_file_output;

        if ((fileOutput != null) && (getDesignFile() != null)) {
            System.out.println("File to save: " + fileOutput.getName());
            System.out.print("File format: ");
            switch (DesignFile.getFileFormat(fileOutput)) {
                case DSN:
                    System.out.println("DSN");
                    break;
                case FRB:
                    System.out.println("FRB");
                    break;
                case SES:
                    System.out.println("SES");
                    break;
                case SCR:
                    System.out.println("SCR");
                    break;
                default:
                    System.out.println("UNKNOWN");
                    break;
            }
            path = fileOutput.getAbsolutePath();
            System.out.println("Path to save at: " + path);

            switch (DesignFile.getFileFormat(fileOutput)) {
                case DSN:
                    this.saveAsSpecctraDesignDsn(fileOutput, fileOutput.getName(), false);
                    break;
                case FRB:
                    this.saveAsBinary(fileOutput);
                    break;
                case SES:
                    this.saveAsSpecctraSessionSes(fileOutput, fileOutput.getName());
                    break;
                case SCR:
                    this.saveAsEagleScriptScr(getDesignFile().getEagleScriptFile(), fileOutput.getName());
                    break;
                default:
                    FRLogger.warn("Saving the board failed, because the selected file format is not supported.");
                    break;
            }

        }
        else {
            System.out.println("No file selected!");
            return false;
        }

        return true;
    }

    /**
     * Saves output file in DSN format
     *
     * @param outputFile
     * @param designName
     * @param compatibilityMode
     * @return
     */
    public boolean saveAsSpecctraDesignDsn(File outputFile, String designName, boolean compatibilityMode)
    {
        if (outputFile == null)
        {
            return false;
        }

        // FRLogger.info("Saving '" + outputFile.getPath() + "'...");
        OutputStream output_stream;
        try
        {
            output_stream = new FileOutputStream(outputFile);
        } catch (Exception e)
        {
            output_stream = null;
        }

        return boardHandling.saveAsSpecctraDesignDsn(output_stream, designName, compatibilityMode);
    }

    /**
     * Saves the board, GUI settings and subwindows to disk as a binary file.
     * Returns false, if the save failed.
     *
     * @param outputFile
     */
    private boolean saveAsBinary(File outputFile)
    {
        if (outputFile == null)
        {
            return false;
        }

        OutputStream output_stream;
        ObjectOutputStream object_stream;
        try
        {
            FRLogger.info("Saving '" + outputFile.getPath() + "'...");

            output_stream = new FileOutputStream(outputFile);
            object_stream = new ObjectOutputStream(output_stream);
        } catch (IOException e)
        {
            return false;
        } catch (Exception e)
        {
            return false;
        }

        // (1) Save the board as binary file
        boolean save_ok = boardHandling.saveAsBinary(object_stream);
        if (!save_ok)
        {
            return false;
        }

        // (2) Flush the binary file
        try
        {
            object_stream.flush();
            output_stream.close();
        } catch (IOException e)
        {
            return false;
        }
        return true;
    }


    /**
     * Writes a Specctra Session File (SES). Returns false, if write operation fails.
     *
     * @param outputFile
     * @param designName    name of the design file
     */
    public boolean saveAsSpecctraSessionSes(File outputFile, String designName)
    {
        if (outputFile == null)
        {
            return false;
        }

        // FRLogger.info("Saving '" + outputFile.getPath() + "'...");
        OutputStream output_stream;
        try
        {
            output_stream = new FileOutputStream(outputFile);
        } catch (Exception e)
        {
            output_stream = null;
        }

        if (!boardHandling.saveAsSpecctraSessionSes(output_stream, designName))
        {
            System.out.println("SPECCTRA SES save failed!");
            return false;
        }

        System.out.println("SPECCTRA SES save successful!");
        return true;
    }

    /**
     * Saves file in SES format.
     *
     * @param outputFile
     * @param design_name
     */
    public void saveAsEagleScriptScr(File outputFile, String design_name)
    {
        ByteArrayOutputStream sesOutputStream = new ByteArrayOutputStream();
        if (!boardHandling.saveAsSpecctraSessionSes(sesOutputStream, design_name))
        {
            return;
        }
        InputStream sesInputStream = new ByteArrayInputStream(sesOutputStream.toByteArray());

        FRLogger.info("Saving '" + outputFile.getPath() + "'...");

        OutputStream output_stream;
        try
        {
            output_stream = new FileOutputStream(outputFile);
        } catch (Exception e)
        {
            output_stream = null;
        }

        if (boardHandling.saveSpecctraSessionSesAsEagleScriptScr(sesInputStream, output_stream))
        {
            System.out.print("File saved as Eagle Script SCR at: " + outputFile.getPath());
        }
        else
        {
            System.out.print("Failed to save as Eagle Script SCR!");
        }
    }

}