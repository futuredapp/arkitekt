is_pr_big = git.lines_of_code > 500
has_correct_prefix = github.branch_for_head.match(/^(feature|hotfix|fix|release|housekeep)\//)

warn("Branch name should have `release/`, `hotfix/`, `fix/`, `housekeep/` or `feature/` prefix.") if !has_correct_prefix
warn("Pull request is classed as Work in Progress") if is_pr_wip
warn("This pull request is too big.") if is_pr_big

commit_lint.check warn: :all, disable: [:subject_length]

# Utils
def report_checkstyle_for_directory(directory_name)
  if Dir.exists?(directory_name)
    Dir.glob(directory_name).each {|f|
      report_checkstyle(directory_name + f)
    }
  end
end

def report_checkstyle(file_name)
  if File.file?(file_name)
    checkstyle_format.report file_name
  end
end

# Setup checkstyle
checkstyle_format.base_path = Dir.pwd

# Detekt checkstyle
report_checkstyle 'build/reports/detekt/detekt.xml'

# Original checkstyle
report_checkstyle 'app/build/reports/checkstyle/checkstyle.xml'

# Ktlint checkstyle
report_checkstyle_for_directory 'app/build/reports/ktlint/'
