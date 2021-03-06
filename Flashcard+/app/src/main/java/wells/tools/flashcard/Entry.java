package wells.tools.flashcard;

public class Entry
{
    protected String mName = null;
    protected String mContent = null;

    public Entry(String name, String content)
    {
        this.mName = name;
        this.mContent = content;
    }

    public void setName(String name)
    {
        this.mName = name;
    }

    public void setContent(String content)
    {
        this.mContent = content;
    }

    public String getName()
    {
        return this.mName;
    }

    public String getContent()
    {
        return this.mContent;
    }
}
