cd plugin
call cd mUpdate
call gradle install
call cd ..
call cd ..
call gradle build tasks --stacktrace