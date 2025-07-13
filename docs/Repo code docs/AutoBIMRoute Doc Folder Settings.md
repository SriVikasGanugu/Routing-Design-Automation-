**Settings:**

This folder contains files defining various configurations and settings in the app.

**ApiServerSettings:**

Manages settings for the API server, including enabling/disabling the server, allowing HTTP connections, and defining server endpoints.

**AutoRouterSettings**: 

Configures the autorouter, controlling parameters like the maximum number of passes, thread count, and strategies for board updates, item selection, and optimization.

**DisabledFeaturesSettings**: 

Specifies which features are disabled in the application, such as logging, multi-threading, macros, and menu options.

**GlobalSettings**: 

Acts as the central configuration, integrating various settings (autorouter, API server, disabled features) and handling file-based storage and command-line argument parsing. It also manages localization and other global parameters.

**UsageAndDiagnosticDataSettings**:

Stores usage and diagnostic data configurations, including user ID, analytics settings, and email, used for tracking or analytics purposes.