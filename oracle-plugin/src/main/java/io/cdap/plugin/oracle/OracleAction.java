/*
 * Copyright © 2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.plugin.oracle;

import com.google.common.collect.ImmutableMap;
import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.cdap.etl.api.action.Action;
import io.cdap.plugin.db.batch.action.AbstractDBAction;
import io.cdap.plugin.db.batch.config.DBSpecificQueryConfig;

import java.util.Map;
import javax.annotation.Nullable;

/**
 * Action that runs Oracle command.
 */
@Plugin(type = Action.PLUGIN_TYPE)
@Name(OracleConstants.PLUGIN_NAME)
@Description("Action that runs Oracle command")
public class OracleAction extends AbstractDBAction {

  private final OracleActionConfig oracleActionConfig;

  public OracleAction(OracleActionConfig oracleActionConfig) {
    super(oracleActionConfig, false);
    this.oracleActionConfig = oracleActionConfig;
  }

  /**
   * Oracle Action Config.
   */
  public static class OracleActionConfig extends DBSpecificQueryConfig {
    @Name(OracleConstants.DEFAULT_BATCH_VALUE)
    @Description("The default batch value that triggers an execution request.")
    @Nullable
    public Integer defaultBatchValue;

    @Name("connectionType")
    @Description("Whether to use an SID or Service Name when connecting to the database.")
    public String connectionType;

    @Override
    public String getConnectionString() {
      if (OracleConstants.SERVICE_CONNECTION_TYPE.equals(this.connectionType)) {
        return String.format(OracleConstants.ORACLE_CONNECTION_STRING_SERVICE_NAME_WITH_DB_FORMAT,
                             host, port, database);
      }
      return String.format(OracleConstants.ORACLE_CONNECTION_STRING_SID_WITH_DB_FORMAT, host, port, database);
    }


    @Override
    protected Map<String, String> getDBSpecificArguments() {
      return ImmutableMap.of(OracleConstants.DEFAULT_BATCH_VALUE, String.valueOf(defaultBatchValue));
    }
  }
}
