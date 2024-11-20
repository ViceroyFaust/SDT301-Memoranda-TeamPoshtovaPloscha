### Quality Policy
> Describe your Quality Policy in detail for this Sprint (remember what I ask you to do when I talk about the "In your Project" part in the lectures and what is mentioned after each assignment (in due course you will need to fill out all of them, check which ones are needed for each Deliverable). You should keep adding things to this file and adjusting your policy as you go.
> Check in Project: Module Concepts document on Canvas in the Project module for more details 

**GitHub Workflow** (start in Sprint 1)
- The main branch is the default branch which should contain stable releases
- The development branch is the stable development branch, which contains only tested code
- User Story branches should have the following format:
  - US#-[short description]
  - e.g. US1-DevelopModels
- Use the template described in this article "[How to Write a Git Commit Message](https://cbea.ms/git-commit/)" to write proper git commit messages
  - Commit messages must include User Story and Task numbers
  - Please set the following as your commit message template (The Lines with # are comments)
```markdown
# 50 characters ##################################
# If applied, this commit will...


# 72 characters ########################################################
# Why is this change needed?
Prior to this change, 

# How does it address the issue?
This change

# Provide links to any relevant tickets, articles or other resources
```
- If you want to merge with Development after you completed your User Story, please submit a pull request
- Pull requests should be fast forwards
  - In order to achieve a fast-forward, merge the development branch _into_ your User Story branch _before_ submitting the pull request
- Pull Requests are approved by the Git Master
  - If the Git Master submits a pull request, then it must be reviewed by the SCRUM Master
- We are _not_ allowed to delete branches

**Unit Tests Blackbox** (start in Sprint 2)
- It is preferred that you whitebox test your code, but you can also use blackbox testing techniques on your code. More
detailed descriptions are provided in whitebox.

**Unit Tests Whitebox** (online: start in Sprint 2, campus: start in Sprint 3)
- You need to make sure that your whitebox tests are testing the full requirements of your class' public functionality
  - Test only public functions
  - Test the full scope of your functionality as designed
  - Make sure that there are no bugs in your tests
  - Try to use test-driven development whenever possible

**Code Review** (online: start in Sprint 2, campus: start Sprint 3)
- Code must compile
- Code must be able to be merged via fast-forward
- Code should not break other functionality
- If functionality can be demonstrated through the GUI, it must work
- All public functions have JavaDoc comments, either based on previously established documentation or documented by the
programmer
