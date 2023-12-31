import pandas as pd
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation

def read_csv_with_varying_columns(file_path, expected_columns):
    data = []

    with open(file_path, 'r') as file:
        for line in file:
            fields = line.strip().split(',')
            if len(fields) == expected_columns:
                data.append(fields)

    df = pd.DataFrame(data)
    return df

def update_plot(frame):
    try:
        df = read_csv_with_varying_columns('spaceXLaunches.csv', expected_columns=5)
    except Exception as e:
        print(f"Error reading CSV file: {e}")
        return

    df.columns = ["mission_name", "launch_date", "launch_vehicle", "customer", "launch_success"]
    launch_dates = pd.to_datetime(df["launch_date"])
    launch_years = launch_dates.dt.year
    launch_counts = launch_years.value_counts().sort_index()

    plt.cla()  # Clear the current axis to update the plot
    # Bar plot
    plt.bar(launch_counts.index, launch_counts.values, color='gray', alpha=0.7, label="Unknown")

    success_counts = df[df["launch_success"] == 'true'].groupby(launch_dates.dt.year)["launch_success"].count()
    fail_counts = df[df["launch_success"] == 'false'].groupby(launch_dates.dt.year)["launch_success"].count()

    success_counts = success_counts.reindex(launch_counts.index, fill_value=0)
    fail_counts = fail_counts.reindex(launch_counts.index, fill_value=0)
   
    plt.bar(success_counts.index, success_counts.values, color='green', alpha=0.7, label="Success")
    plt.bar(fail_counts.index, fail_counts.values, bottom=success_counts.values, color='red', alpha=0.7, label="Fail")

    # Line plot
    plt.plot(launch_counts.index, launch_counts.values, color='red', marker='o', linestyle='dashed', linewidth=2, markersize=8, label="Total Launches")

    plt.xlabel("Year", labelpad=10)
    plt.ylabel("Number of Launches")
    plt.title("Launch Frequency")
    plt.xticks(rotation=45, ha="right")
    plt.xticks(range(min(launch_years), max(launch_years) + 1))

    plt.legend()
    plt.grid(True)
    plt.tight_layout()

# Set up the initial plot
plt.figure(figsize=(10, 6))
ani = FuncAnimation(plt.gcf(), update_plot, interval=1000)  
plt.show()
