# General Terminology:

This section is to help readers familiarize themselves with the general terminology before they proceed through the documentation to understand the files and application features. The terminology will cover the basics of electrical engineering and circuit design.

### 1. Trace:

      A conductor in the PCB that helps propagate signals between components in the PCB. (something like a small wire connecting different pins, and pads in the PCB).

### 2. Via:

      Conductive paths that establish electrical connections between PCB layers.

### 3. Routing:

      Process of connecting different components in the PCB with the help of traces.

### 4. Net:

      Electrical connections between components. Each net contains all the components to be connected with traces.

### 5. Net Class:

      Set of nets, which follow the same rules.

### 6. Clearance:

      Distance between two differnet components in the board.

### 7. Rules:

      Predefined constraints and guidelines which help in PCB design.

### 8. Padstacks:

      Set of shapes and properties that define the geometry of a pad (or via) on each layer of the PCB.


### 9. Pins:

      Electrical connection point of a component that will connect to the PCB.

### 10. Clearance Violation:

      Violations raised when the distance between two components is less than the distance allowed(close distance causes inductance between the components of the board.

### 11. Length Violation:

      Violations raised when the length of the trace is longer than a defined value(we have to check this to reduce costs).

# DSN File Documentation:

The .DSN file is a PCB design file. It is structured in a hierarchical format, defining different aspects of the PCB layout, including layers, components, connections, and design rules. Below is a breakdown of the keywords and their meanings, along with an explanation of how the connections and layers are managed:

## Overview of Key Terms in the DSN File:

### 1. PCB:

The PCB keyword defines the start of the PCB design and its associated metadata. </br>
Example: (pcb "TestSensel-KiCad6.dsn") </br>
This includes the path to the file and settings related to the PCB. </br>

### 2. Parser:

This section contains parsing rules for reading the file. It includes attributes like **string_quote**, **space_in_quoted_tokens**, and **host_cad**. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; a. **String_quote:** This defines the character representing a quotation mark within the file. Quotation marks are often necessary to encapsulate strings with spaces or special characters. The presence of string_quote ")" means that a closing parenthesis (typically used for closing a keyword definition) is used as a quotation mark or delimiter for strings in this file.</br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; b. **space_in_quoted_tokens:** This specifies whether spaces are allowed inside quoted tokens. If set to on, the parser will accept spaces within strings enclosed by quotes.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; c. **Host_cad:** This keyword defines the CAD software used to create or edit the PCB design.

### 3. Resolution and unit:

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; a. Resolution defines the unit resolution, and the unit specifies the measurement unit used (in this case, micrometers - um). </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; b. Example: (resolution um 10) </br>

### 4. Structure:

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The structure section defines the physical layers and boundaries of the PCB. It includes the following subsections: </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **I. Layer:** Defines individual layers of the PCB. For instance, F.Cu and B.Cu represent the front and back copper layers typically used for routing signals. </br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **II. Type:** Defines the type of layer (e.g., signal, indicating it carries electrical signals).

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **III. Property:** It defines various characteristics/attributes of the layer/component/design element. There can be other properties like layer visibility and conditions. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1. Index: One such property used here is index. It is used as a unique identifier for the layer. For example, if there are F.Cu and B.Cu layers, we can refer to F.Cu (front layer) as index 0 and B.Cu as index 1.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **IV.** Boundary: Specifies the physical dimensions of the PCB layout using coordinates. The parameters used in the boundary block are: </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1. Path: The path keyword defines a sequence of coordinates that form the outline or boundary of the PCB. It describes a polygon or closed shape using a series of points connected by straight lines.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 2. Pcb: The keyword pcb in this context indicates that the defined boundary is for the entire PCB (as opposed to other potential elements like a component or region). The points following the PCB keyword define the board's shape, typically in micrometers (since the file uses um as the unit). </br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 3. The Co-ordinates: The coordinates are generally written in (x,y) format. The series of points are connected in sequence to form the boundary. These points define the vertices of the polygon that outlines the PCB. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; For example, </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; (path pcb 0 129000 -65000 117000 -65000 117000 -48000 129000 -48000 129000 -65000) </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; a. Default Path Type: 0 after pcb might be a placeholder or default value indicating that this path is not associated with a particular signal layer but instead defines a mechanical boundary for the entire PCB. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; b. Breakdown of the Coordinates: </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; i. 129000 -65000: This is the first point on the boundary (top-right corner). </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ii. 117000 -65000: This is the second point (moves left from the first point along the top boundary). </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; iii. 117000 -48000: This is the third point (moves down from the second point along the left boundary). </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; iv. 129000 -48000: This is the fourth point (moves right from the third point along the bottom boundary). </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; v. 129000 -65000: This is the fifth point, which connects back to the first point to close the boundary, form closed rectangular shape. </br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **V. Via:** A via is a small conductive hole allowing electrical connections between printed circuit board layers (PCB). Vias are essential for multi-layer PCBs, where components on one layer may need to connect to traces or components on another. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The notation Via [0-1] \_700:300_um provides specific details about the via's type, size, and connection layers. Here's a breakdown of this notation:

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1. Via [0-1]: This indicates that the via connects layer 0 to layer 1. The via transfers a signal from the front copper layer (F.Cu) to the back copper layer (B.Cu). </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 2. 700:300: These numbers specify the size dimensions of the via. 700 represents the outer diameter of the via (including the hole and the surrounding copper pad) in micrometers (um). 300 represents the diameter of the drill hole (the actual hole drilled through the PCB) in micrometers. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 3. \_um: The unit of measurement is micrometers (um). </br>

 <div align="center">

   <img src="/docs/photos/uiSidebar/AppUI-Sidebar.png" alt="Sidebar Preview" width="714" height="549">

_The sidebar options displayed on the left as indicated by the arrow_

   </div>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **VI. Rule:** Specifies design rules, including width and clearance. These rules specify the minimum acceptable dimensions for specific physical features on the board, such as the width of traces and the clearance (spacing) between components and conductive elements. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **Example:** </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; (rule </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; (width 254) </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; (clearance 152.5) </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; (clearance 152.5 (type default_smd)) </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; (clearance 38.1 (type smd_smd)) </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ) </br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Breakdown of the rule Example: </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1. (rule): The rule keyword defines the design rules that will be applied to the layout of the PCB. </br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 2. (width 254): </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Definition: This specifies the default width of the PCB traces (electrical pathways).
A trace on a PCB is essentially a flat, narrow conductor (made of copper) that connects components and allows current to flow between them, like how wires would function in a circuit. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Value: 254 is measured in the unit defined in the file (likely micrometers, or µm). </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Usage: The design's standard trace width is 254 µm. This value affects the minimum width of copper traces on the board, essential for ensuring adequate current capacity and manufacturability. </br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 3. (clearance 152.5): </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Definition: This sets the general clearance, or the minimum spacing between conductive elements (such as traces, vias, and pads).
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Value: 152.5 µm. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Usage: The clearance ensures that conductive elements on the PCB do not come too close to each other, preventing electrical shorts or interference. In this case, the default clearance between different conductive elements is 152.5 µm. </br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 4. (clearance 152.5 (type default_smd)): </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Definition: This specifies the clearance between the default surface-mount devices (SMDs - a contact point for soldering the component to the PCB) and other conductive elements on the PCB. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Value: 152.5 µm. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Usage: For standard surface-mounted components (SMDs), the clearance rule is the same as the default clearance—152.5 µm. This ensures adequate spacing between SMD pads and other copper elements for soldering and electrical performance. </br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 5. (clearance 38.1 (type smd_smd)): </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Definition: This specifies the clearance between surface-mount device (SMD) pads. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Value: 38.1 µm. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Usage: This rule applies to spacing adjacent pads on the same SMD component. The 38.1 µm clearance is smaller than the default clearance, allowing for closer spacing between the pads of an individual SMD. This is typical for components with fine-pitch leads, where pads must be placed closer together. </br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;6. Summary:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Width: Sets the trace width to 254 µm. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Clearance: Sets the general clearance between different conductive elements to 152.5 µm. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Clearance for SMDs: </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The clearance between SMD pads and other elements is also 152.5 µm. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The clearance between SMD pads (within the same component) is smaller, at 38.1 µm, allowing for tighter packing of pads on small components. </br>

### 5. Placement:

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; This section defines where components are placed on the board, including details such as component name, position, layer (front or back), and orientation (angle). </br>
For Example, </br>
(placement </br>
(component Resistor_SMD:R_0603_1608Metric (place R2 118973.580000 -50688.600000 front 0.000000 (PN R_Small_US))) </br>
(component MyLibs:PCB_Fork_10mils_5mmx5mm (place J4 123500.000000 -59500.000000 front 0.000000 (PN Conn_01x02_Male)))</br>
) </br>

Breakdown of the placement Example: </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1. (component Resistor_SMD: R_0603_1608Metric): This line describes the component being placed.
Resistor_SMD:R_0603_1608Metric: This is the footprint or symbol for the component, specifically a surface-mount resistor with a standard package size of R_0603 (1608 metric).
The "0603" refers to the imperial dimensions (0.06 x 0.03 inches), and "1608" is the metric equivalent (1.6 mm x 0.8 mm). </br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 2. (place R2 118973.580000 -50688.600000 front 0.000000 (PN R_Small_US)): </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;i. Place R2: This refers to the specific reference designator of the component, in this case, R2, which means it is the second resistor in the schematic.
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ii. 118973.580000 -50688.600000: These are the X and Y coordinates of the component on the PCB in micrometers (µm). These coordinates tell the manufacturing equipment exactly where to place the component on the board. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;X = 118973.580000 µm: Horizontal position. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Y = -50688.600000 µm: Vertical position. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;iii. front: This indicates the component is placed on the front layer of the PCB (F.Cu, the top copper layer). </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; iv. 0.000000: This represents the rotation angle of the component. In this case, the component is not rotated and is placed at a 0° orientation. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; v. (PN R_Small_US): The resistor's Part Number (PN), denoted here as R_Small_US. This might refer to a part number used in the bill of materials (BOM) to order components. </br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 3. (component MyLibs: PCB_Fork_10mils_5mmx5mm): </br>
This is another component being placed using a custom library or symbol named MyLibs. </br>
PCB_Fork_10mils_5mmx5mm refers to a component in this custom library. In this case, it seems to be some kind of connector or fork with a size of 5 mm x 5 mm, with 10 mils (0.254 mm) pitch. </br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 4. (place J4 123500.000000 -59500.000000 front 0.000000 (PN Conn_01x02_Male)): </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;place J4: The component reference designator is J4, indicating a connector (usually J is used for connectors). </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;123500.000000 -59500.000000: This component's X and Y coordinates on the PCB. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;X = 123500.000000 µm. </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Y = -59500.000000 µm.</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;front: This indicates that the connector J4 is placed on the front side of the PCB.</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;0.000000: The component is placed with a 0° rotation, meaning it is not rotated.</br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(PN Conn_01x02_Male): This is the Part Number for the connector, specifying it is a 1x2 male pin header (possibly a 2-pin connector). </br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 5. Summary of the Placement Example: </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;R2: </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Component: A surface-mount resistor (R_0603). </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Position: X = 118973.580000 µm, Y = -50688.600000 µm (front layer). </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Rotation: 0° (no rotation). </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Part Number: R_Small_US.</br> </br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;J4:</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Component: A connector or fork (custom footprint).</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Position: X = 123500.000000 µm, Y = -59500.000000 µm (front layer).</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Rotation: 0° (no rotation).</br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Part Number: Conn_01x02_Male (a 2-pin male connector).</br>

# App Interface

**I. Sidebar**

   <div align="center">

   <img src="/docs/photos/uiSidebar/AppUI-Sidebar.png" alt="Sidebar Preview" width="714" height="549">

_The sidebar options displayed on the left as indicated by the arrow_

   </div>

The sidebar on the left side of the window pane allows you to generate the possible routes, undo or redo your actions, zoom in and out, and toggle the clearance violation visibility.

1. Start auto-router and route optimizer

   ![Start Auto-Router](/docs/photos/uiSidebar/AppUI-Gen.png)

   &nbsp;&nbsp;&nbsp;&nbsp;Begin potential route generation and optimization.

2. Stop the current process

   ![Stop Process](/docs/photos/uiSidebar/AppUI-Stop.png)

   S&nbsp;&nbsp;&nbsp;&nbsp;top the route generation.

3. Delete all tracks

   ![Delete Tracks](/docs/photos/uiSidebar/AppUI-Delete.png)

   &nbsp;&nbsp;&nbsp;&nbsp;Delete all existing tracks and viases on the board to start board routing from scratch.

4. Undo last action

   ![Undo Last Action](/docs/photos/uiSidebar/AppUI-Undo.png)

   &nbsp;&nbsp;&nbsp;&nbsp;Undo the last action performed.

5. Redo last action

   ![Redo last action](/docs/photos/uiSidebar/AppUI-Redo.png)

   &nbsp;&nbsp;&nbsp;&nbsp;Redo the last action undone.

6. Toggle rats nests visibility

   ![Toggle Rats Nests](/docs/photos/uiSidebar/AppUI-RatsNest.png)

   &nbsp;&nbsp;&nbsp;&nbsp;Display rats nests on the board.

7. Toggle clearance violations

   ![Toggle Clearance Violations](/docs/photos/uiSidebar/AppUI-Constraint.png)

   &nbsp;&nbsp;&nbsp;&nbsp;Display clearance violations on the board.

8. Zoom into selected region

   ![Zoom In](/docs/photos/uiSidebar/AppUI-ZoomIn.png)

   &nbsp;&nbsp;&nbsp;&nbsp;Zoom into the region you clicked on.

9. Zoom out to the whole board

   ![Zoom Out](/docs/photos/uiSidebar/AppUI-ZoomOut.png)

   &nbsp;&nbsp;&nbsp;&nbsp;Zoom out to view the whole board.

10. Open settings

    ![Open Settings](/docs/photos/uiSidebar/AppUI-Settings.png)

&nbsp;&nbsp;&nbsp;&nbsp;View additional auto-router settings (see Section II.c.3.c).

11. Create and modify tracks and traces

    ![Create Tracks Mode](/docs/photos/uiSidebar/AppUI-Trace.png)

&nbsp;&nbsp;&nbsp;&nbsp;Enables manual creation of tracks and traces.

12. Drag components or traces

    ![Drag Mode](/docs/photos/uiSidebar/AppUI-Drag.png)

&nbsp;&nbsp;&nbsp;&nbsp;Enables dragging of components and traces.

**II. General Settings**

The General Settings window allows you to toggle the selectability of the PCB components between layers.

<div align="center">

<img src="/docs/photos/uiSidebar/AppUI-GeneralSettings.png" alt="General Settings Window" width="302" height="478">

_General Settings window_

</div>

1. Selection Layers: Toggle whether all the visible components or only those within the selected layer can be selected.

2. Selectable Items: Toggle which circuit board components can be selectable.

3. Current Layer: Toggle between circuit board layers.

**III. Dropdown Menu**

   <div align="center">

   <img src="/docs/photos/uiDropdownMenu/DropdownMenu.png" alt="Dropdown Menu Preview" width="283" height="52">

   </div>
   
   The dropdown menu contains features within the application which can help change user view, change settings and modify PCB.

1. File:

   ![FileOperations](/docs/photos/uiDropdownMenu/FileOp.png)

   1. Open: Helps open files(Specctra or .DSN files).

   2. Save as: Saves changes made in the file.

   3. Exit: Close the application.

2. Appearance:

   ![AppearanceOperations](/docs/photos/uiDropdownMenu/AppearanceOp.png)

   These settings mainly modify the appearance of components in the PCB.

   1. Object Visibility:

      ![Object Visibility](/docs/photos/uiDropdownMenu/Appearance1.png)

      This primarily helps manipulate the visibility of the PCB's traces, vias, and several other objects.

   2. Layer Visibility:

      ![Layer Visibility](/docs/photos/uiDropdownMenu/Appearance2.png)

      These help modify the visibility of the layers(Copper Layers) in the PCB.

   3. Colors:

      ![Colors](/docs/photos/uiDropdownMenu/Appearance3.png)

      This helps change colors of components in the PCB.

   4. Miscellaneous:

      ![Miscellaneous](/docs/photos/uiDropdownMenu/Appearance4.png)

      These include other settings in the application like cursor, layer, and board.

3. Settings: Helps change settings in the app.

   1. General: Detailed description of the General Settings is available above.

   2. -> Routing:

      ![Routing Settings](/docs/photos/uiDropdownMenu/Settings2.png)

      These settings help modify the rules for routing, this will give the flexibility to optimize routing in the PCB.

      1. Snap-angle:

      Angle at which the routes are placed in the PCB. We have 2 options: 45 degrees and 90 degrees, we can choose either option for our routing. (In practice 45-degree routes are preferred over 90 degrees because the 90-degree routes tend to take up more area risking the possibility of inductance.)

      2. Route Mode:

      There are two options here dynamic and stitching, dynamic routing means the routing will adjust based on a set of criteria like the shortest path or the route adjusts as obstacles appear like components or traces on the path. Stitching is a technique used to improve grounding or to connect different layers in a PCB.

      3. Rule Selection:

      Here we have two options automatic and manual, automatic rule selection is where the software selects design rules based on pre-defined settings. Manual rule selection enables the user to manually select and set routing rules.

      4. Push and Shove Enabled:

      Enables the circuit designer or user to adjust other components and traces while routing between components.

      5. Drag components Enabled:

      Enables repositioning of components along with routes(pre-connected) adjusting to the component's new positions.

      6. Vias snap to smd center:

      Ensures that vias snap to the surface mount devices when placed near them.

      7. Highlight routing obstacle:

      Highlight obstacles(other traces or components) in the routing process to avoid routing around them. Helps reduce clearance violations.

      8. Ignore conduction areas:

      Helps ignore conduction areas(areas where there are a lot of components and traces).

      9. Automatic neckdown:

      Process of automatically reducing trace thickness in conduction areas.

      10. Restrict pin-exit directions:

      Limits pin-exit directions, useful for controlling how traces fan out from components to maintain orderly routing.

      11. Pad to turn gap:

      Defines the minimum distance from a pad to a turn in the route.

      12. Pull tight region:

      Controls how aggressively the router pulls traces tight to obstacles or nearby traces. A higher number indicates more slack, whereas a lower number pulls tight the traces to the obstacles.

   -> Detail Parameter:

   ![Detail Routing Parameter Settings](/docs/photos/uiDropdownMenu/DetailParameter.png)

   1. Clearance Compensation:

   This feature helps decide if the software(for routing) should consider clearance violations or not. It provides 2 options to choose if we want to consider clearance violations(On and Off).

   2. Pull Tight Accuracy:

   Increases pull-tight accuracy while routing, higher accuracy means that the routes will have small gaps. Lower accuracy pertains to looser and less optimized paths.

   3. Keepout outside board outline:

   This helps keep the routes within the PCB design boundary.

   3. -> Auto-router:

      ![Auto-router Settings](/docs/photos/uiDropdownMenu/Settings3.png)

      These settings help modify the auto-routing process in the application. Changes in these settings decide the quality of produced routes.

      1. Active:

      It sets if the autorouter should be active or inactive on a layer of the PCB.

      2. Preferred Direction:

      This defines which direction we would like our routes(through the autorouter) to be on each layer.

      3. Vias allowed:

      This allows the auto-router to connect components through vias in PCB.

      4. Passes:

      Options about the auto-router.

      5. Fanout:

      This is related to arranging traces connected to components.

      6. Auto-routing:

      Enables auto-routing in the app.

      7. Optimization:

      Enables optimization of routes(shortens trace length, via counts or grounding, etc.) with the auto-router.

   -> Detail Auto Parameter:

   ![Detail Auto Parameter Settings](/docs/photos/uiDropdownMenu/DetailAutoParameter.png)

   1. Via costs:

   Cost or penalty assigned to vias.

   2. Plane Via costs:

   Refers to the cost of using vias within a specific plane.

   3. Start pass:

   Starting pass for the auto-routing algorithm.

   4. Start ripup costs:

   Relates to re-routing traces while auto-routing.

   5. Speed:

   This refers to the speed of the auto-routing process. Faster auto-routing may lead to less optimized routing.

   6. Trace costs on layer:

   Routing costs with respect to different layers.

   7. In-preferred direction:

   Routing costs in preferred direction(i.e. Vertical or horizontal).

   8. Against preferred direction:

   Routing costs in opposite direction.

   4. Controls:

      ![Controls](/docs/photos/uiDropdownMenu/Settings4.png)

      These provide help for component placement and view manipulation.

      1. Horizontal component grid:

      Helps align components uniformly horizontally in the PCB.

      2. Vertical Component grid:

      Helps align components uniformly vertically in the PCB.

      3. Wheel function:

      Options for mouse feel function, to zoom the PCB design or rotate the PCB.

4. Rules:

   1. Clearance Matrix:

      ![Clearance Matrix](/docs/photos/uiDropdownMenu/Rules1.png)

      Provides information about the distance between from one component to other components and provides an option(trim) to decrease the distance manually. Add class helps another class of components to the existing matrix. Layer provides option to view a clearance matrix only on the specified layer.

   2. Vias:

      ![Vias](/docs/photos/uiDropdownMenu/Rules2.png)

      Provides information on Vias in the PCB design with flexibility to add or remove vias.

      1. Available Via Padstacks:

      Provides a list of all via components connected with pads.

      2. Available Vias:

      Provides a list of all Vias in the PCB.

      3. Via Rules:

      There are various types of vias in a PCB, each type follows specific rules depending on the requirements.

      4. Info:

      Provides information along with coordinates of the via.

      5. Create:

      Helps create a new via for the PCB.

      6. Remove:

      Removes a via from the pre-defined list.

      7. Edit:

      Helps edit the name of a via rule.

   3. Nets:

      ![Nets](/docs/photos/uiDropdownMenu/Rules3.png)

      Defines and provides information on all existing electrical connections in the PCB.

      1. Assign Class:

      Allows the user to assign a class to a group of nets.

      2. Filter Incompletes:

      Filters all incomplete nets in the list of nets.

      3. Filter:

      Allows to user to filter through the list for required net.

   4. Net Classes:

      ![Net Classes](/docs/photos/uiDropdownMenu/Rules4.png)

      Groups nets into different classes, which helps define unique rules for each class.

      1. Clearance class:

      Defines clearance requirements for the component.

      2. Trace width:

      Width of trace for each net.

      3. On layer:

      Provides information on which layer the net is on.

      4. Shove fixed:

      Option to fix all trace positions or route positions in the net.

      5. Cycles with area:

      Related to how the net interacts with copper areas.

      6. Length:

      Information on minimum and maximum length of trace in the net.

      7. Ignored by auto router:

      Option to exclude nets from auto-routing.

      8. Add:

      Adds a net class to the list.

      9. Remove:

      Helps remove a net class from the list.

      10. Assign:

      Helps assign nets and clearance classes to a net class.

      11. Select:

      Helps select a net class in the PCB.

      12. Show Nets:

      Shows all the nEts associated with a net class.

      13. Filter Incompletes:

      Filters all incompletes in the list.

5. Info:

   This app feature helps provide information about the PCB design. It includes information on what consists of the PCB, like packages, padstacks, violations, etc. This helps select the components and objects in the PCB design and modify their behavior for the PCB, it allows to get information, delete, autoroute components, and many more.

   ![Info Dropdown](/docs/photos/uiDropdownMenu/InfoOp.png)

   All information in the dropdown menu is derived from the dsn file. Each feature contains 4 options:

   -> Info:

   ![Info](/docs/photos/uiDropdownMenu/Info.png)

   This provides information about the component with exact coordinates in the PCB design.

   -> Select:

   ![Select](/docs/photos/uiDropdownMenu/Select.png)

   This highlights the chosen component in the PCB design.

   -> Invert:

   ![Invert](/docs/photos/uiDropdownMenu/Invert.png)

   This feature chooses all other components in the list.

   -> Recalculate:

   This feature recalculates or verifies if the chosen component is of the chosen category, if it is not it removes it from the list.

   1. Library Packages:

      ![Library Packages](/docs/photos/uiDropdownMenu/Info1.png)

      Helps view packages used in the PCB design. Allows inspection, selection, or filter package types in design and apply changes.

   2. Library Padstacks:

      ![Library Padstacks](/docs/photos/uiDropdownMenu/Info2.png)

      Helps view padstacks used in the PCB design. Allows inspection, selection or filtering of package types in design.

   3. Placed Components:

      ![Placed Components](/docs/photos/uiDropdownMenu/Info3.png)

      Shows components with details already placed in the PCB. Allows inspection, selection or filtering of package types in design.

   4. Incompletes:

      ![Incompletes](/docs/photos/uiDropdownMenu/Info4.png)

      Helps identify any incomplete connections in the PCB design, such as unrouted traces or missed connections. Provides information on incompletes to resolve them by recaluculation.4

   5. Length Violations:

      ![Length Violations](/docs/photos/uiDropdownMenu/Info5.png)

      Help identify length violations in the PCB design. Provides information on length violations to help recalculate and resolve them.

   6. Clearance Violations:

      ![Clearance Violations](/docs/photos/uiDropdownMenu/Info6.png)

      Helps identify clearance violations in the PCB design. Provides information on clearance violations to resolve them with recalculation.

   7. Unconnected Routes:

      ![Unconnected Routes](/docs/photos/uiDropdownMenu/Info7.png)

      Helps identify unconnected routes in the PCB. Provides information to help resolve them with recalculation of appropriate routes.

   8. Route Stubs:

      ![Route Stubs](/docs/photos/uiDropdownMenu/Info8.png)

      Helps identify routes stubs in the PCB design and provides information to resolve them with recalucultion of appropriate connections.

   -> Toolbar:

   After selection of components a toolbar which is displayed.

   ![Toolbar](/docs/photos/uiDropdownMenu/Toolbar.png)

   1. Cancel:

   This button likely cancels the current operation, stopping whatever action or command you were performing.

   2. Info:

   Displays information about the selected object or operation. This might show details about a specific component, net, or connection.

   3. Delete:

   Deletes the selected object, such as a connection, component, or route.

   4. Cutout:

   This could be used to create a "cutout" area, which would define a specific space or region in the design where routing or components are restricted or removed.

   5. Fix:

   Locks the selected component, net, or route in place to prevent any changes or movement.

   6. Unfix:

   Unlocks a previously fixed component or route, allowing it to be edited or moved again.

   7. Autoroute:

   Automatically generates routing paths for connections between components based on the layout.

   8. Pull Tight:

   This might optimize or clean up the routes by pulling the wires or traces to make them more efficient or compact.

   9. Fanout:

   Typically used in PCB design to create fanout patterns from a component's pins to make routing easier, especially for devices like BGAs (Ball Grid Arrays).

   10. Clearance:

   Ensures the specified minimum spacing between components, nets, or routes to avoid short circuits or interference.

   11. Nets:

   Allows the selection or management of electrical connections or nets in the design.

   12. Conn. Sets:

   Likely refers to "Connection Sets," which could allow you to define and manage a group of related connections.

   13. Connections:

   Displays or highlights the current electrical connections or traces between components.

   14. Components:

   Manages the components in the design, possibly allowing you to place, move, or configure them.

   15. New Net:

   Creates a new electrical connection or net between components.

   16. New Component:

   Adds a new component to the design layout.

   17. Violations:

   Highlights any design violations, such as clearance errors, unconnected nets, or overlapping components.

   18. Zoom Selection:

   Zooms in on the currently selected object or area of the design.

   19. Zoom All:

   Zooms out to display the entire design.

   20. Zoom Region:

   Allows you to zoom in on a specific region by selecting it.

6. Help:

   ![Help](/docs/photos/uiDropdownMenu/Help1.png)

   Information on the App, regarding the version, and method to contact the developers.

3) **.dsn Files**
