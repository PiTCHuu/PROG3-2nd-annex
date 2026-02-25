package modules;

public class Vote {
    private int id;
    private Integer candidateId;
    private int voterId;
    private VoteTypes voteTypes;

    public Vote(int id, Integer candidateId, int voterId, VoteTypes voteTypes) {
        this.id = id;
        this.candidateId = candidateId;
        this.voterId = voterId;
        this.voteTypes = voteTypes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }

    public int getVoterId() {
        return voterId;
    }

    public void setVoterId(int voterId) {
        this.voterId = voterId;
    }

    public VoteTypes getVoteTypes() {
        return voteTypes;
    }

    public void setVoteTypes(VoteTypes voteTypes) {
        this.voteTypes = voteTypes;
    }
}