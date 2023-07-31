import datetime
import os

# define a version constant
VERSION = "1.0.0"
OS_NAME = "baseline"

print("hello thales!-whoo!")

# create a text file with some data inside
def create_file():
    # open or create a file under target folder
    path = "target"
    # create directory if does not exist
    if not os.path.exists(path):
        print("Creating directory: " + path)
        os.makedirs(path)      
    else:
        # clear directory
        print("Clearing directory: " + path)
        for file in os.listdir(path):
            os.remove(path + "/" + file)
    
    # name convention is ddmmyyyy_hhmmss.log
    fileName = f"{OS_NAME}_{VERSION}.log"
    f = open(path + "/" + fileName, "w+")
    for i in range(10):
        f.write("This is line %d\n" % (i+1))
    f.close()

create_file()