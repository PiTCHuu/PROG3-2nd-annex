package Services;

import modules.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    private DBConnection dbConnection;
    public DataRetriever() {
        this.dbConnection = new DBConnection();
    }

    public long countAllVotes() {
        String sql = "SELECT COUNT(id) AS total_votes FROM vote";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getLong("total_votes");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<VoteTypeCount> countVotesByType() {

        String sql = """
        SELECT vote_type, COUNT(id) AS count
        FROM vote
        GROUP BY vote_type
        ORDER BY vote_type
    """;

        List<VoteTypeCount> results = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                String voteType = rs.getString("vote_type");
                long count = rs.getLong("count");

                results.add(new VoteTypeCount(voteType, count));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    public List<CandidateVoteCount> countValidVotesByCandidate() {

        String sql = """
        SELECT c.name AS candidate_name,
               COUNT(CASE WHEN v.vote_type = 'VALID' THEN 1 END) AS valid_vote
        FROM candidate c
        LEFT JOIN vote v ON c.id = v.candidate_id
        GROUP BY c.name
        ORDER BY c.name
    """;

        List<CandidateVoteCount> results = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                String candidateName = rs.getString("candidate_name");
                long validVote = rs.getLong("valid_vote");

                results.add(new CandidateVoteCount(candidateName, validVote));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    public VoteSummary computeVoteSummary() {

        String sql = """
        SELECT
            COUNT(CASE WHEN vote_type = 'VALID' THEN 1 END) AS valid_count,
            COUNT(CASE WHEN vote_type = 'BLANK' THEN 1 END) AS blank_count,
            COUNT(CASE WHEN vote_type = 'NULL' THEN 1 END) AS null_count
        FROM vote
    """;

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            if (rs.next()) {
                long validCount = rs.getLong("valid_count");
                long blankCount = rs.getLong("blank_count");
                long nullCount = rs.getLong("null_count");

                return new VoteSummary(validCount, blankCount, nullCount);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new VoteSummary(0, 0, 0);
    }

    public double computeTurnoutRate() {

        String sql = """
        SELECT
            (COUNT(DISTINCT voter_id)::decimal / (SELECT COUNT(id) FROM voter)) AS turnout_rate
        FROM vote
    """;

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("turnout_rate") * 100;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    public ElectionResult findWinner() {

        String sql = """
        SELECT c.name AS candidate_name,
               COUNT(CASE WHEN v.vote_type = 'VALID' THEN 1 END) AS valid_vote_count
        FROM candidate c
        LEFT JOIN vote v ON c.id = v.candidate_id
        GROUP BY c.name
        ORDER BY valid_vote_count DESC
        LIMIT 1
    """;

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            if (rs.next()) {
                String name = rs.getString("candidate_name");
                long validVotes = rs.getLong("valid_vote_count");
                return new ElectionResult(name, validVotes);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
