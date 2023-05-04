# Runway Redecalaration Project

This project is designed to calculate the new runway parameters based on the user input for runway redeclaration. 

## Dependencies

The application requires JavaFX to run. Please ensure that JavaFX is installed on your computer before running the application.
You may download JavaFX component [here](https://gluonhq.com/products/javafx/).

## How to Run

1. Open a terminal or command prompt and type `java -version` to check if Java is installed on your computer. If Java is not installed, please install it before proceeding.

2. Download and decompress the zip file, navigate to the folder containing the `RunwayRedeclaration.jar` file. (it is in the 'code' folder)

3. Type the following command to run the application:

   `java --module-path /path/to/javafx/lib --add-modules=javafx.controls,javafx.fxml,javafx.graphics -jar RunwayRedeclaration.jar`

   Replace `/path/to/javafx/lib` with the path to the directory containing the JavaFX runtime components on your system. This path will depend on the location where you installed JavaFX on your computer.

4. Press Enter to run the command. The application should launch and you can now input the required data for runway redeclaration.

## How to Use

Once the application is launched, you will see a form on the left hand side that you need to fill out either by selecting options or typing in values. Some menus have been disabled to ensure that the required information is selected before proceeding. Once all fields are completed, click on the "Perform Calculation" button to calculate the new runway parameters. 

The calculated results will be displayed at the right side of the interface, in the form of table and calculation breakdown. Visualisation will also be displayed in the middle of the interface, with tabs to navigate between the three types of views, namely top-down, side-on and simultaneous views.

Please refer to User Guide for more detailed explanation. Application demo is available here: https://youtu.be/R-0Q2KT8cWA.
