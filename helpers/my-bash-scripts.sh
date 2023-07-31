# function commit code and push
# should have one parameter for the commit message
gacp() {
    git add .
    git commit -m "$1"
    git push
}

# add the following line to your .bashrc or .bash_profile
# source ~/path/to/test1/helpers/my-bash-scripts.sh