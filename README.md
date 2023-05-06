![cover page (2)]()

# Runway Redeclaration Project

**Group Number**: Group 1 

**Languages**: JAVA, XML 

**Module**: COMP2211 Software Engineering Group Project

# About the Application

This tool is designed to perform runway redeclaration for UK commercial airports using calculations and process sprcified by the CAA. Given standard runway information and information about an obstacle, the tool will provide the revised runway parameters together with a visualisation of the obstacle and a summary of the calculations. 

Some basic functionalities of the system:
- Perform calculations to redeclare runway parameters
  - View calculation breakdowns
  - View result as graphical visualisations (top-down, side-on and simultaneous view)
  - Generate redeclaration report
- Import and export airport data (UK commercial airport)
- Manage users for the system

## Running the Application

The application requires JavaFX to run. Please ensure that JavaFX is installed on your computer before running the application.
You may download JavaFX component [here](https://gluonhq.com/products/javafx/).

1. Navigate to the Project_Repository and download the zip, decompress the zip file and navigate to the folder containing the `RunwayRedeclaration.jar` file.

2. Open a terminal or command prompt and type `java -version` to check if Java is installed on your computer. If Java is not installed, please install it before proceeding.

3. Type the following command to run the application:

   `java --module-path /path/to/javafx/lib --add-modules=javafx.controls,javafx.fxml,javafx.graphics -jar RunwayRedeclaration.jar`

   Replace `/path/to/javafx/lib` with the path to the directory containing the JavaFX runtime components on your system. This path will depend on the location where you installed JavaFX on your computer.

4. The application should now launch and you can input the required data for runway redeclaration.

# More Info
Each folder in this repository contains documentation and incremental codes for the project. Submodule Project_Repository is forked from the actual project repository for the group project.
