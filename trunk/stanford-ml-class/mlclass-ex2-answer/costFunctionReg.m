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


z = X * theta;
g = sigmoid(z);

J = (1 / m) * sum((-y' * log(g) - (1 - y') * log(1 - g))) + lambda / (2 * m) * sum(theta(2:size(theta)) .^ 2);

for j = 1:size(theta),
    if j == 1,
        grad(j) = (1 / m) * sum((g - y) .* X(:,j));
    else if j >= 2,
        grad(j) = (1 / m) * (sum((g - y) .* X(:,j)) + lambda * theta(j));
    end;

end



% =============================================================

end
