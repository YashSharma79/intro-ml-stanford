function [C, sigma] = dataset3Params(X, y, Xval, yval)
%DATASET3PARAMS returns your choice of C and sigma for Part 3 of the exercise
%where you select the optimal (C, sigma) learning parameters to use for SVM
%with RBF kernel
%   [C, sigma] = DATASET3PARAMS(X, y, Xval, yval) returns your choice of C and 
%   sigma. You should complete this function to return the optimal C and 
%   sigma based on a cross-validation set.
%

% You need to return the following variables correctly.
C = 1;
sigma = 0.3;

% ====================== YOUR CODE HERE ======================
% Instructions: Fill in this function to return the optimal C and sigma
%               learning parameters fox2d using the cross validation set.
%               You can use svmPredict to predict the labels on the cross
%               validation set. For example, 
%                   predictions = svmPredict(model, Xval);
%               will return the predictions on the cross validation set.
%
%  Note: You can compute the prediction error using 
%        mean(double(predictions ~= yval))
%
possibleValues = [0.01, 0.03, 0.1, 0.3, 1, 3, 10, 3];

min_error = 1000000;
opt_C = 0;
opt_Sigma = 0;

for i=1:length(possibleValues),
    for j=1:length(possibleValues),
        model = svmTrain(X, y, possibleValues(i), @(X, y) gaussianKernel(X, y, possibleValues(j)));
        prediction = svmPredict(model, Xval);
        error_cv = mean(double(prediction ~= yval));
        if error_cv < min_error,
            min_error = error_cv;
            opt_C = possibleValues(i);
            opt_Sigma = possibleValues(j);
        end;
    end;
end;

C = opt_C;
sigma = opt_Sigma;






% =========================================================================

end
