package modules;

public class VoteTypeCount {

    private String voteType;
    private long count;

    public VoteTypeCount(String voteType, long count) {
        this.voteType = voteType;
        this.count = count;
    }

    @Override
    public String toString() {
        return "VoteTypeCount(voteType=" + voteType + ", count=" + count + ")";
    }
}
