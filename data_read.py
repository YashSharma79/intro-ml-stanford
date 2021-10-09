import os
import pandas as pd

data_file = os.path.join('..', 'data', 'house_tiny.csv')
data = pd.read_csv(data_file)
print(data)