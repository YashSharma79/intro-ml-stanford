function [J, grad] = costFunctionReg(theta, X, y, lambda)
%COSTFUNCTIONREG Compute cost and gradient for logistic regression with regularization
%   J = COSTFUNCTIONREG(theta, X, y, lambda) computes the cost of using
%   theta as the parameter for regularized logistic regression and the
%   gradient of the cost w.r.t. to the parameters. 

% Initialize some useful values
m = length(y); % number of training examples

% You need to return the following variables correctly 
J = 0;
grad = zeros(size(theta));

% ====================== YOUR CODE HERE ======================
% Instructions: Compute the cost of a particular choice of theta.
%               You should set J to the cost.
%               Compute the partial derivatives and set grad to the partial
%               derivatives of the cost w.r.t. each parameter in theta



sigmoid = sigmoid(X*theta);
logSig = log(sigmoid);
logOneMinusSig = log(1 - sigmoid);
firstTerm = -y .* logSig;
secondTerm = (1-y).*(logOneMinusSig);
regCoefficient = (lambda/(2*m));
regTerm = sum(theta(2:end,:).^2);
J = ((1/m) * sum(firstTerm - secondTerm)) + (regCoefficient * regTerm);

for j=1:size(X,2),
t = sum((sigmoid - y).*X(:,j));
t = t *(1/m);
    if j>1,
        t = t + ((lambda/m)* theta(j));
    end;
grad(j) = t;
end;



% =============================================================

end
