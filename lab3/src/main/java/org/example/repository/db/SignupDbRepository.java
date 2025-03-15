package org.example.repository.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.domain.Event;
import org.example.domain.Signup;
import org.example.domain.Tuple;
import org.example.repository.interf.ISignupRepository;
import org.example.utils.CommonUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SignupDbRepository implements ISignupRepository {
    private CommonUtils Utils;
    private static final Logger logger = LogManager.getLogger();

    public SignupDbRepository(Properties properties) {
        logger.info("Initializing ChildDbRepository with properties: {}", properties);
        this.Utils = new CommonUtils(properties);
    }
    @Override
    public Iterable<Signup> GetAllByChildId(Integer id) {
        logger.traceEntry("Looking for signup with child id: {} ", id);
        Connection connection = this.Utils.GetConnection();
        List<Signup> signups = new ArrayList<>();
        try (var statement = connection.prepareStatement("select * from signups where id_child = ? "))
        {
            statement.setInt(1, id);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    signups.add( (Signup) new Signup()
                            .SetId(new Tuple<>(resultSet.getInt(1), resultSet.getInt(2)))
                    );
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error" + e);
        }
        logger.traceExit();
        return signups;
    }

    @Override
    public Iterable<Signup> GetAllByEventId(Integer id) {
        logger.traceEntry("Looking for signup with event id: {} ", id);
        Connection connection = this.Utils.GetConnection();
        List<Signup> signups = new ArrayList<>();
        try (var statement = connection.prepareStatement("select * from signups where id_event = ? "))
        {
            statement.setInt(1, id);
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    signups.add( (Signup) new Signup()
                            .SetId(new Tuple<>(resultSet.getInt(1), resultSet.getInt(2)))
                    );
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error" + e);
        }
        logger.traceExit();
        return signups;
    }

    @Override
    public Signup FindById(Tuple<Integer, Integer> integerIntegerTuple) {
        logger.traceEntry("Looking for signup with id: {} {}", integerIntegerTuple.GetFirst(), integerIntegerTuple.GetSecond());
        Connection connection = this.Utils.GetConnection();
        Signup signup = null;
        try (var statement = connection.prepareStatement("select * from signups where id_child = ? and id_event = ?"))
        {
            statement.setInt(1, integerIntegerTuple.GetFirst());
            statement.setInt(2, integerIntegerTuple.GetSecond());
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    signup = (Signup) new Signup()
                            .SetId(new Tuple<>(resultSet.getInt(1), resultSet.getInt(2)));
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error" + e);
        }
        logger.traceExit();
        return signup;
    }

    @Override
    public Iterable<Signup> FindAll() {
        logger.traceEntry("Getting all of the signups.");
        Connection connection = this.Utils.GetConnection();
        List<Signup> signups = new ArrayList<>();
        try (var statement = connection.prepareStatement("select * from signups")) {
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next())
                    signups.add(
                            (Signup) new Signup()
                                    .SetId(new Tuple(resultSet.getInt(1), resultSet.getInt(2)))
                    );
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error" + e);
        }
        logger.traceExit();
        return signups;
    }

    @Override
    public void Save(Signup entity) {
        logger.traceEntry("Saving signup " + entity);
        Connection connection = this.Utils.GetConnection();
        try (var statement = connection.prepareStatement("insert into signups (id_child, id_event) values (?, ?)"))
        {
            statement.setInt(1, entity.GetChild().GetId());
            statement.setInt(2, entity.GetEvent().GetId());
            int result = statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error " + e);
        }
        logger.traceExit();
    }

    @Override
    public Signup Update(Signup entity) {
        return null;
    }

    @Override
    public Signup Delete(Tuple<Integer, Integer> integerIntegerTuple) {
        return null;
    }
}
