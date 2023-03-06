# Errors on Push 
## Rejected - non-fast-forward 
You need to pull first.

# Errors on Pull
## Checkout conflict
You have a local change that you did not commit. 
You can 1) commit without pushing you local change or 2) delete your local change by replacing the changed files/folders with a previous version.
Then you will be able to pull. This will maybe result in a conflict, if your colleagues changed the same lines.
After resolving the conflict, commit all changed files and push.

# Problems of Pull Requests
## Changes outside of package
You are changing files outside of your java package. You can see the files that you are changing in the "Files changed" tab of your pull request on the github website. This list must contain only files in your package.

You can replace them with the version from the main repository. To do that, right click on these files or folders -> Replace with -> Branch, Tag or Reference -> Remote tracking -> select the main repository -> Replace. If the files or folders do not exist in the main repository, you can simply delete them. Then commit and push to your repository. Don't close the pull request, it will be automatically updated.

## Hidden changes outside of package
You are changing some files or folders that are hidden in the Java perspective of Eclipse. You can see the files that you are changing in the "Files changed" tab of your pull request on the github website. This list must contain only files in your package.

To see the files, go to the Resource perspective of Eclipse. Right click on these files or folders -> Replace with -> Branch, Tag or Reference -> Remote tracking -> select the main repository -> Replace. Then commit and push to your repository. Don't close this pull request, it will be automatically updated.

## Additions outside of the package
In the Resource perspective you can replace all folders and files (including hidden files). Since there is no .metadata folder in the main repository, you don't have anything to replace it with. You can simply delete it.

## Compilation error
Your code has a compilation error (red mark in Eclipse). You can read the error message by clicking on "Details" near to the failed test at the bottom of your pull request. Please fix it and commit+push again.

## Execution error
Not accepting because the code does not pass the tests. You can run the same tests on your Eclipse, they are `test/abstraction/FiliereParDefaultTest.java` and `test/presentation/FenetrePrincipaleTest.java`. To run them right-click on "build.xml", Run As -> Ant Build. You will see the error message in the console.

## Correcting the pull request
No need to close and reopen the pull request, you can just fix the problems and push to your repository.
