# **Data Streaming Project**
## Streaming with Flink

This project is a streaming data processing application built with Apache Flink that fetches SpaceX launch events from the SpaceX API, processes the data, and prints the raw events. The main components include a LaunchEvent case class, a custom timestamp assigner (LaunchTimestampAssigner), and a source function (SpaceXApiSource) to fetch and parse SpaceX launches.

## Prerequisites

Ensure you have the following installed:

- IntelliJ IDEA with Scala plugin
- Apache Flink dependencies added to your project (as specified in pom.xml)

## How to Run

1. *Setup Project:*
   - Open IntelliJ IDEA.
   - Clone this repository.
   - Refresh the project to download the dependencies.

2. *Run the Application:*
   - Locate the AverageSpaceXLaunches class containing the main method.
   - Right-click on the file or the main method and select "Run AverageSpaceXLaunches."

3. *View Output:*
   - The application prints raw SpaceX launch events to the console.

4. *CSV Output:*
   - The CsvLaunchEventSink class appends launch events to a CSV file . Check the file path specified in the filePath variable to verify the CSV file is being created and updated.
  
## Code Overview

- SpaceXApiSource: Fetches SpaceX launch events from an API source.
- LaunchTimestampAssigner: Assigns timestamps and watermarks to the launch events.
- CsvLaunchEventSink: Appends launch events to a CSV file.

The application is currently configured to print raw events. 


## Visualization with Python: FuncAnimation in Matplotlib

The `plot.py` script generates an animated plot visualizing the launch frequency of SpaceX missions over the years. The script reads data from the CSV file (`spaceXLaunches.csv`) which is located in the same directory as the script, and then uses the Pandas and Matplotlib libraries to create a dynamic plot.

## Prerequisites

Ensure you have the following installed before running the script:

- Python (version 3.x recommended)
- Pandas library
- Matplotlib library

Install the required libraries using the following commands:

```bash
pip install pandas matplotlib
```

## Usage

Run the script in a terminal using the following command:

```bash
python plot.py
```

This will execute the script, and a Matplotlib window will appear displaying the animated launch frequency plot. The data for the plot is read from the `spaceXLaunches.csv` file, which should be present in the same directory as the script.

## Script Overview

The script performs the following steps:

1. Reads CSV data from `spaceXLaunches.csv`.
2. Processes the data and creates a Pandas DataFrame.
3. Extracts launch dates and calculates the launch frequency over the years.
4. Generates a bar plot for total launches, distinguishing between success and failure.
5. Displays an animated plot using Matplotlib.

![Alt Text](img/result_viz.gif)

Happy plotting! 🚀

## *Simultaneous Execution:*
   - For comprehensive insights, run both the Flink streaming application and the Python plotting script simultaneously.
   - Observe the live updates in the console output of the Flink application and the dynamic changes in the Matplotlib plot.
