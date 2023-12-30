import pandas as pd
import matplotlib.pyplot as plt
from datetime import datetime
import time

def read_csv_with_varying_columns(file_path, expected_columns):
    data = []

    with open(file_path, 'r') as file:
        for line in file:
            # Split the line into fields using a comma as the delimiter
            fields = line.strip().split(',')

            # Check if the number of fields matches the expected number
            if len(fields) == expected_columns:
                data.append(fields)

    # Create a DataFrame from the collected data
    df = pd.DataFrame(data)

    return df

while True:
    try:
        # Read CSV file without header, skipping lines with errors
        df = read_csv_with_varying_columns('spaceXLaunches.csv', expected_columns=5)
    except Exception as e:
        print(f"Error reading CSV file: {e}")
        continue  # Skip to the next iteration

    # Convert the second column (index 1) to datetime format
    df[1] = pd.to_datetime(df[1])

    # Plotting
    plt.figure(figsize=(10, 6))
    for index, row in df.iterrows():
        plt.plot(row[1], index, marker='o', linestyle='-', color='b', label=row[0])

    plt.title('Satellite Launch Timeline')
    plt.xlabel('Launch Date')
    plt.ylabel('Satellite Index')
    plt.legend()
    plt.grid(True)
    plt.show()

    # Pause for a while before checking for updates
    time.sleep(60)  # Check every minute, for example
