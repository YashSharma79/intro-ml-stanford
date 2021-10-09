inputs, outputs = data.iloc[:, 0:2], data.iloc[:,2]
inputs = inputs.fillna(inputs.mean())
print(inputs)