var now = new Date(); 
var hours = now.getHours(); 
var minutes = now.getMinutes(); 
var timeValue = "" + ((hours >12) ? hours -12 :hours) 
timeValue += ((minutes < 10) ? ":0" : ":") + minutes
timeValue += (hours >= 12) ? " PM" : " AM" 
timerRunning = true; 

mydate = new Date(); 
myday = mydate.getDay(); 
mymonth = mydate.getMonth(); 
myweekday= mydate.getDate(); 
weekday= myweekday; 
myyear= mydate.getYear(); 
year = myyear

if(myday == 0) 
day = " Domingo, " 

else if(myday == 1) 
day = " Segunda, " 

else if(myday == 2) 
day = " Terça, " 

else if(myday == 3) 
day = " Quarta, " 

else if(myday == 4) 
day = " Quinta, " 

else if(myday == 5) 
day = " Sexta, " 

else if(myday == 6) 
day = " Sábado, " 

if(mymonth == 0) 
month = "/01/ " 

else if(mymonth ==1) 
month = "/02/ " 

else if(mymonth ==2) 
month = "/03/" 

else if(mymonth ==3) 
month = "/04/" 

else if(mymonth ==4) 
month = "/05/"

else if(mymonth ==5) 
month = "/06/" 

else if(mymonth ==6) 
month = "/07/" 

else if(mymonth ==7) 
month = "/08/" 

else if(mymonth ==8) 
month = "/09/ " 

else if(mymonth ==9) 
month = "/10/" 

else if(mymonth ==10) 
month = "/11/" 

else if(mymonth ==11) 
month = "/12/" 