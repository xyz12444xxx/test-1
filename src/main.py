import datetime
import os

print("hello thales!")

# create a text file with some data inside
def create_file():
    # open or create a file under target folder
    path = "target"
    # create directory if does not exist
    if not os.path.exists(path):
        print("Creating directory: " + path)
        os.makedirs(path)                
    
    # name convention is ddmmyyyy_hhmmss.log
    fileName = datetime.datetime.now().strftime("%d%m%Y_%H%M%S") + ".log"
    f = open(path + "/" + fileName, "w+")
    for i in range(10):
        f.write("This is line %d\n" % (i+1))
    f.close()

create_file()