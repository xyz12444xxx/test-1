import datetime


print("hello thales!")

# create a text file with some data inside
def create_file():
    # open or create a file under target folder
    # name convention is ddmmyyyy_hhmmss.log
    fileName = datetime.datetime.now().strftime("%d%m%Y_%H%M%S") + ".log"
    f = open("target/" + fileName, "w+")
    for i in range(10):
        f.write("This is line %d\n" % (i+1))
    f.close()

create_file()