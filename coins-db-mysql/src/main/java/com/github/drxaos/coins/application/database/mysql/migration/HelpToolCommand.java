package com.github.drxaos.coins.application.database.mysql.migration;

public class HelpToolCommand extends ToolCommand {

    @Override
    public void execute() {

        System.err.println("MigrationTool <command>");
        System.err.println("commands:");
        System.err.println("  diff - print changesets to migrate from current db state to generated from entities");
        System.err.println("  migrate - apply all changesets from changelog");
        System.err.println("  release - create sql files for last release");

    }
}
