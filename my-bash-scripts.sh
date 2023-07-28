# function commit code and push
# should have one parameter for the commit message
gitpush() {
    git commit -m "$1"
    git push
}