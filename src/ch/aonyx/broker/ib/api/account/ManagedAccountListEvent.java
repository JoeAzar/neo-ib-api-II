/*
 * Copyright (C) 2012 Aonyx
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.aonyx.broker.ib.api.account;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import ch.aonyx.broker.ib.api.AbstractEventSupport;

/**
 * @author Christophe Marcourt
 * @since 1.0.0
 */
public final class ManagedAccountListEvent extends AbstractEventSupport {

    public static final char SEPARATOR = ',';
    private final String commaSeparatedAccountList;
    private char separator = SEPARATOR;
    private final Function<String, String> toTrimmedStringFunction = new Function<String, String>() {
        @Override
        public String apply(final String input) {
            return StringUtils.trim(input);
        }
    };

    public ManagedAccountListEvent(final String commaSeparatedAccountList) {
        super();
        this.commaSeparatedAccountList = commaSeparatedAccountList;
    }

    public String getCommaSeparatedAccountList() {
        return commaSeparatedAccountList;
    }

    public List<String> getAccounts() {
        return Collections.unmodifiableList(Lists.transform(
                Lists.newArrayList(StringUtils.split(commaSeparatedAccountList, separator)), toTrimmedStringFunction));
    }

    public void setSeparator(final char separator) {
        this.separator = separator;
    }

    @Override
    public Class<?> getAssignableListenerType() {
        return ManagedAccountListEventListener.class;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
