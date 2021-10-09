import os

os.makedirs(os.path.join('..', 'data'), exist_ok = True)
data_file = os.path.join('..', 'data', 'house_tiny.csv')

with open(data_file, 'w') as f:
    f.write('NumRooms,Alley,Price\n') #column names
    f.write('2,Pave,60\n')
    f.write('NA,NA,60\n')
    f.write('NA,NA,50\n')
    f.write('5,NA,55\n')