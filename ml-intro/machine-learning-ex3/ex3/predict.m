function p = predict(Theta1, Theta2, X)
%PREDICT Predict the label of an input given a trained neural network
%   p = PREDICT(Theta1, Theta2, X) outputs the predicted label of X given the
%   trained weights of a neural network (Theta1, Theta2)

% Useful values
m = size(X, 1);
num_labels = size(Theta2, 1);

% You need to return the following variables correctly 
p = zeros(size(X, 1), 1);

% ====================== YOUR CODE HERE ======================
% Instructions: Complete the following code to make predictions using
%               your learned neural network. You should set p to a 
%               vector containing labels between 1 to num_labels.
%
% Hint: The max function might come in useful. In particular, the max
%       function can also return the index of the max element, for more
%       information see 'help max'. If your examples are in rows, then, you
%       can use max(A, [], 2) to obtain the max for each row.
%

%adding bias units in input layer
X =  [ones(size(X,1),1) X];

%propogate to the next layer by multiplyinh the training samples and the weight parameters
z2 = X*Theta1';

%sigmoid for the classfication of the layer
sig = sigmoid(z2);

%adding bias units in hidden layer
sig = [ones(size(sig,1), 1) sig];

z3 = sig*Theta2';
a3 = sigmoid(z3);
[x, ix] = max(a3, [], 2);
p = ix;





% =========================================================================


end
