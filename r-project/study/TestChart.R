# TODO: Add comment
# 
# Author: nhn
###############################################################################


install.packages("iplots",dep=TRUE)

library(iplots)
library(RODBC)
data = odbcConnectExcel(file.choose())
sqlTables(data)
mydat = sqlFetch(data, "data1")

x <- mydat$salary
y <- mydat$career
Dname <- mydat$name
Deval <- mydat$eval
Dcareer <- mydat$career

attach(mydat)
iplot(x, y, title="Plot Chart", vfont=20)

iabline(lm(y ~ x), col = "#ff0000")  # regression line (y~x)
ilines(lowess(x,y), col = "#0000c0") # lowess line (x,y)

itext(x,y, labels="2", font=20)


while (!is.null(ievent.wait())) {
	iobj.opt(visible=FALSE)
	
	if (iset.sel.changed() && length(iset.selected()) != 0) {
		s <- iset.selected()
		itext(x[s],y[s], ax=-0.2, ay=-0.2, labels=paste(Dname[s],":\n",Deval[s],":",Dcareer[s]))
		
	} else {
		iobj.opt(visible=FALSE)
		
	}
	
}


itext(x[s],y[s], ax=-0.2, ay=-0.2, labels=paste(Dname[1],":",Dcareer[1]))


