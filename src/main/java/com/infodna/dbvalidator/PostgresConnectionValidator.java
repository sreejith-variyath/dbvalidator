package com.infodna.dbvalidator;

import com.izforge.izpack.api.data.InstallData;
import com.izforge.izpack.api.installer.DataValidator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by sreejith on 10/21/2018.
 */
public class PostgresConnectionValidator implements DataValidator {

    protected InstallData installData;
    protected String str_errorMsg;
    protected String str_warningMsg = "";
    private static final String STR_DEFAULT_ERROR_MESSAGE = "Cannot connect to the specified database.";


    @Override
    public Status validateData(InstallData installData) {
        this.installData = installData;
        try {

            Class.forName(getDriver());

        } catch (ClassNotFoundException e) {

            str_errorMsg = "Problem during loading db-driver.";
            return Status.ERROR;

        }
        Connection connection = null;

        try {

            connection = DriverManager.getConnection(getPostgresUrl(), getUser(), getPassword());

        } catch (SQLException e) {

            str_errorMsg = "Could not connect to database: \n\n" + e.getLocalizedMessage();
            return Status.ERROR;

        }
        return Status.OK;
    }

    @Override
    public String getErrorMessageId() {
        if (str_errorMsg != null) {
            return str_errorMsg;
        } else {
            return STR_DEFAULT_ERROR_MESSAGE;
        }
    }

    @Override
    public String getWarningMessageId() {
        return str_warningMsg;
    }

    @Override
    public boolean getDefaultAnswer() {
        return true;
    }

    public String getDriver() {
        return "org.postgresql.Driver";
    }

    public String getHost() {
        return installData.getVariable("postgres.host");
    }

    public String getPort() {
        return installData.getVariable("postgres.port");
    }

    public String getInstance() {
        return installData.getVariable("postgres.instance");
    }

    public String getQuery() {
        return "SELECT 1";
    }

    public String getUser() {
        return installData.getVariable("postgres.user");
    }

    public String getPassword() {
        return installData.getVariable("postgres.password");
    }

    public String getPostgresUrl() {
        //"jdbc:postgresql://127.0.0.1:5432/infodna", "postgres", "Welcome2017"
        StringBuffer url = new StringBuffer("jdbc:postgresql://" + getHost() + "/" + getInstance());
        return url.toString();

    }
}
