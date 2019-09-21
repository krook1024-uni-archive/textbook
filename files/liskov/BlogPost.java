class BlogPost {
    String content;
    public String getContent()  {
        return content;
    }
}

class VideoBlogPost extends BlogPost {
    int videoId;
    public String getContent() {
        return "<iframe src=\"https://youtu.be/embed/ " + videoId+  "\"></iframe>";
    }
}
