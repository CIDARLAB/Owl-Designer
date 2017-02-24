**Owl-CAT: Designer**
------------
OwlDesigner is one of the three base module application of Owl-CAT (Cluster Assembly Tool) Software: the Designer, Builder, and Packager.

 1. Designer: **this application**. Standalone application.
 2. Builder (i.e., Eugene): [GitHub Repo Link](https://github.com/CIDARLAB/eugene-v2.0). Standalone application.
 3. Packager: [GitHub Repo Link](https://github.com/CIDARLAB/Owl-Packager).

## **Installation** ##
Use Maven to package a single jar file, bundled with all dependencies:

    mvn clean package
then go to /target/ and find **eugeneDesigner-0.0.1-SNAPSHOT-jar-with-dependencies.jar** file.

## **RUN** ##
OwlDesigner is a command line application. Use -help to see the list of arguments:

    java -jar eugeneDesigner-0.0.1-SNAPSHOT-jar-with-dependencies.jar -help

> Written with [StackEdit](https://stackedit.io/).