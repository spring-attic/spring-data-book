srcDir = "./data/"

// use the shell (made available under variable fsh)
dir = "/data/apache/logs"
if (!fsh.test(dir)) {
   fsh.mkdir(dir); 
   fsh.copyFromLocal(srcDir, dir); 
   fsh.chmod(700, dir)
}