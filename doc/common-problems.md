# Problems on Push

## "Rejected - non-fast-forward"

A team mate pushed something new to your team repository before you.

1. Commit your changes.
2. Pull from your team repository.
3. Resolve conflicts if needed.
4. Commit and push.

# Problems on Pull

## "Checkout conflict" or "DIRTY_WORKTREE"

You have a local change that you did not commit. 

1. You can A) commit without pushing your local change or B) delete your local change by replacing the changed files/folders with a previous version.
2. Pull from your team repository.

This will maybe result in a conflict, if your team mate changed the same lines. 
In this case you will have to resolve the conflict before being able to commit your changes and push.

# Problems on Pull Request

If the pull request is correct, it is merged automatically (within 5 minutes) and it is removed from the list of open pull requests in the main repository. If you still see your pull request, it means that there is some problem you will need to solve as soon as possible. The problems of your pull requests are listed at the bottom of the pull request page on github. The 3 possible kinds of problem are described next.

Note that there is no need to close and reopen the pull request, you can just fix the problems and push to your repository.

## "Code owner review required"

You are changing files outside of the java package of your team. You can see the files that you are changing in the "Files changed" tab of your pull request on the github website. This list must contain only files in your package.

You can replace these files with the version from the main repository. To do that, right click on these files or folders -> Replace with -> Branch, Tag or Reference -> Remote tracking -> select the main repository (not your team repository) -> Replace. Then commit and push to your repository. Don't close the pull request, it will be automatically updated.

Note that if the files you changed are not Java files, they will be hidden in the Package Exporer of Eclipse. To see and edit them, click the three dots in the top bar of Package Exporer, select "Filters..." and remove the check from ".* resources". 

## "Some checks were not successful": "Java CI / build (pull_request)"

Not accepting because the code does not pass the tests. You can read the error message by clicking on "Details" near to the failed test at the bottom of your pull request. You can run the same tests on your Eclipse, they are `test/abstraction/FiliereParDefaultTest.java` and `test/presentation/FenetrePrincipaleTest.java`. To run them right-click on "build.xml", Run As -> Ant Build. You will see the error message in the console. Fix the error and commit+push again.

## "This branch is out-of-date with the base branch"

Your team repository is not up-to-date with the changes that happened on the main repository.  Pull from the main repository and then push to the team repository.
