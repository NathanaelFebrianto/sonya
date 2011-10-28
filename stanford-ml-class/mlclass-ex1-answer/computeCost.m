function J = computeCost(X, y, theta)
%COMPUTECOST Compute cost for linear regression
%   J = COMPUTECOST(X, y, theta) computes the cost of using theta as the
%   parameter for linear regression to fit the data points in X and y

% Initialize some useful values
m = length(y); % number of training examples

% You need to return the following variables correctly 
J = 0;

% ====================== YOUR CODE HERE ======================
% Instructions: Compute the cost of a particular choice of theta
%               You should set J to the cost.

m = size(X, 1); % number of training examples
predictions = theta(1,:).* X(:,1) + theta(2,:).* X(:,2); % predictions of hypothesis on all m examples
sqrErrors = (predictions - y(:,1)).^2; % squared errors
sqrErrors;
J = 1/(2*m)*sum(sqrErrors);

% =========================================================================

end
