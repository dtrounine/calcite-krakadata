/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.calcite.sql.ddl.utils;

import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.ddl.SqlColumnDeclaration;
import org.apache.calcite.sql.ddl.SqlColumnDeclarationAutoincrement;
import org.apache.calcite.sql.ddl.SqlPrimaryKeyAutoincrement;

import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * Helper functions for parsing DDL.
 */
public class DdlParserUtils {

  private DdlParserUtils() {}

  public static void postParseTableElements(List<SqlNode> list) {
    SqlPrimaryKeyAutoincrement autoincrementConstraint = null;
    for (SqlNode element : list) {
      if (element instanceof SqlPrimaryKeyAutoincrement) {
        autoincrementConstraint = (SqlPrimaryKeyAutoincrement) element;
      }
    }
    if (autoincrementConstraint != null) {
//          System.out.println("  Autoincrement column: " + autoincrementConstraint.columnList);
      final Set<String> autoincrementColumnNames = new HashSet<>();
      for (SqlNode columnNode : autoincrementConstraint.columnList) {
        final String columnName = ((SqlIdentifier) columnNode).getSimple();
        autoincrementColumnNames.add(columnName);
      }
      final ListIterator<SqlNode> nodeIterator = list.listIterator();
      while (nodeIterator.hasNext()) {
        final SqlNode node = nodeIterator.next();
        if (node instanceof SqlColumnDeclaration) {
          final SqlColumnDeclaration originalDecl = (SqlColumnDeclaration) node;
          final String columnName = originalDecl.name.getSimple();
          if (autoincrementColumnNames.contains(columnName)) {
            final SqlColumnDeclarationAutoincrement newDecl =
                new SqlColumnDeclarationAutoincrement(originalDecl.getParserPosition(),
                    originalDecl.name,
                    originalDecl.dataType,
                    originalDecl.expression,
                    originalDecl.strategy);
            nodeIterator.set(newDecl);
          }
        }
      }
    }
  }

}
