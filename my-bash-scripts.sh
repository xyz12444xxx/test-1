# function commit code and push
# should have one parameter for the commit message
gacp() {
    git add .
    git commit -m "$1"
    git push
}