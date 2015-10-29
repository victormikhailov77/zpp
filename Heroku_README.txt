How to deploy application to Heroku cloud hosting:

Prerequisites:

-user has Heroku account
-toolbelt installed on dev machine
-user logged in, via heroku login command


1. Create application and remote git repository

heroku create zpdevtest

(expected: repo created at: https://git.heroku.com/zpdevtest.git)

2. Push to heroku repo

git push heroku master

3. In case of remote repo name conflict

git remote -v
git remote rm heroku
git remote add heroku https://git.heroku.com/zpdevtest.git

and again:
git push heroku master

4. Configuring application:

heroku ps:scale web=1

5. Open app URL:

heroku open
or go to URL zpdevtest.herokuapp.com in browser

6. Watch logs:

heroku logs --tail


Stop the app:

heroku ps:scale web=0