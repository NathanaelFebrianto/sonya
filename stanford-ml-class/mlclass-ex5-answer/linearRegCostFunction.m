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

J = 1 / (2 * m) * sum((X * theta - y) .^2) + lambda / (2 * m) * sum(theta(2:size(theta)) .^ 2);

for j = 1:size(theta),
    if j == 1,
        grad(j) = (1 / m) * sum((X * theta - y) .* X(:,j));
    else if j >= 2,
        grad(j) = (1 / m) * sum((X * theta - y) .* X(:,j)) + (lambda / m) * theta(j);
    end;

end

%%%%%%%%%%%%%%%%%%%%%%%%%
%J = sum((X*theta - y).^2)/(2*m) + lambda*(sum((theta(2:size(theta),1)) .^2))/(2*m);
%
%grad(1) = sum((X*theta - y).*X(:,1))/m;
%for iter = 2:size(theta,1)
%	grad(iter) = (sum((X*theta - y).*X(:,iter))+lambda*theta(iter))/m;
%end
%%%%%%%%%%%%%%%%%%%%%%%%%







% =========================================================================

grad = grad(:);

end
