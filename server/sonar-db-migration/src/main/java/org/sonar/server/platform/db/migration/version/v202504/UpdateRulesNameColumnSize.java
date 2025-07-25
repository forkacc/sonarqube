/*
 * SonarQube
 * Copyright (C) 2009-2025 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.platform.db.migration.version.v202504;

import java.sql.SQLException;
import org.sonar.db.Database;
import org.sonar.server.platform.db.migration.def.VarcharColumnDef;
import org.sonar.server.platform.db.migration.sql.AlterColumnsBuilder;
import org.sonar.server.platform.db.migration.step.DdlChange;

import static org.sonar.db.DatabaseUtils.tableColumnExists;

public class UpdateRulesNameColumnSize extends DdlChange {
  private static final String TABLE_NAME = "rules";
  private static final String COLUMN_NAME = "name";
  private static final int NEW_COLUMN_SIZE = 255;

  public UpdateRulesNameColumnSize(Database db) {
    super(db);
  }

  @Override
  public void execute(Context context) throws SQLException {
    try (var connection = getDatabase().getDataSource().getConnection()) {
      if (tableColumnExists(connection, TABLE_NAME, COLUMN_NAME)) {
        var columnDef = VarcharColumnDef.newVarcharColumnDefBuilder()
          .setColumnName(COLUMN_NAME)
          .setLimit(NEW_COLUMN_SIZE)
          .build();
        context.execute(new AlterColumnsBuilder(getDialect(), TABLE_NAME)
          .updateColumn(columnDef)
          .build());
      }
    }
  }
}
