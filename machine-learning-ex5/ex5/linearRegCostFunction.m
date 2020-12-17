function [J, grad] = linearRegCostFunction(X, y, theta, lambda)
%LINEARREGCOSTFUNCTION Compute cost and gradient for regularized linear 
%regression with multiple variables
%   [J, grad] = LINEARREGCOSTFUNCTION(X, y, theta, lambda) computes the 
%   cost of using theta as the parameter for linear regression to fit the 
%   data points in X and y. Returns the cost in J and the gradient in grad

% Initialize some useful values
m = length(y); % number of training examples

% You need to return the following variables correctly 
J = 0;
grad = zeros(size(theta));

% ====================== YOUR CODE HERE ======================
% Instructions: Compute the cost and gradient of regularized linear 
%               regression for a particular choice of theta.
%
%               You should set J to the cost and grad to the gradient.
%

h = X * theta;
a = sum((h-y).^2);
a = (1/(2*m)) * a;
regCoefficient = lambda/ (2*m);
regTerm = sum(theta(2:end,:).^2);
J =  a + (regCoefficient * regTerm);

% =========================================================================
for j=1:size(X,2),
    t = sum((h-y).*X(:,j));
    t = t * (1/m);
    grad(j) = t;
    if j > 1,
        reg = (lambda/m) * theta(j);
        grad(j) = grad(j) + reg;
    end;
end;

%regTerm = (lambda/m) * theta;
%regTerm(1,:) = 0;
%grad = grad + regTerm;
grad = grad(:);

end
