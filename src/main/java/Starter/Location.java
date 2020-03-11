package Starter;

public enum Location {
    Path;
    public StringBuilder getPath(){
        String[] Urls=this.getClass().getClassLoader().getResource("soar.exe").getPath().
                replaceAll("%20"," ").split("/");
        StringBuilder urlBuilder=new StringBuilder();
        for(int i=1;i<Urls.length-2;++i){
            urlBuilder.append(Urls[i]+"/");
        }
        return urlBuilder;
    }
}
