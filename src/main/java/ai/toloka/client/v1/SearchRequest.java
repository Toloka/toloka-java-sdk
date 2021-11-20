/*
 * Copyright 2021 YANDEX LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.toloka.client.v1;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public abstract class SearchRequest extends AbstractRequestParameters implements RequestParameters {

    private static final String SORT_PARAMETER = "sort";
    private static final String LIMIT_PARAMETER = "limit";

    private final Map<String, Object> filterParameters;
    private final Map<String, Object> rangeParameters;
    private final String sortParameter;
    private final Integer limit;

    protected SearchRequest(Map<String, Object> filterParameters, Map<String, Object> rangeParameters,
                            String sortParameter, Integer limit) {

        this.filterParameters = filterParameters;
        this.rangeParameters = rangeParameters;
        this.sortParameter = sortParameter;
        this.limit = limit;
    }

    @Override public Map<String, Object> getQueryParameters() {
        Map<String, Object> params = new HashMap<>();
        params.putAll(filterParameters);
        params.putAll(rangeParameters);
        params.put(SORT_PARAMETER, sortParameter);
        params.put(LIMIT_PARAMETER, limit);
        return filterNulls(params);
    }

    public abstract static class Builder<
            G extends SearchRequest,
            T extends Builder,
            F extends FilterBuilder<?, T, ?>,
            R extends RangeBuilder<?, T, ?>,
            S extends SortBuilder<?, T, ?>> {

        protected final F filterBuilder;
        protected final R rangeBuilder;
        protected final S sortBuilder;

        private Integer limit;

        protected Builder(F filterBuilder, R rangeBuilder, S sortBuilder) {
            this.filterBuilder = filterBuilder;
            this.filterBuilder.setBuilder(this);

            this.rangeBuilder = rangeBuilder;
            this.rangeBuilder.setBuilder(this);

            this.sortBuilder = sortBuilder;
            this.sortBuilder.setBuilder(this);
        }

        public F filter() {
            return filterBuilder;
        }

        public R range() {
            return rangeBuilder;
        }

        public S sort() {
            return sortBuilder;
        }

        @SuppressWarnings("unchecked")
        public T limit(int limit) {
            this.limit = limit;
            return (T) this;
        }

        public abstract G done();

        protected Integer getLimit() {
            return limit;
        }
    }

    abstract static class SegmentBuilder<B extends Builder> {

        private B builder;

        public B and() {
            return builder;
        }

        @SuppressWarnings("unchecked")
        void setBuilder(Builder searchRequestBuilder) {
            this.builder = (B) searchRequestBuilder;
        }
    }

    public abstract static class FilterBuilder<
            T extends FilterBuilder, B extends Builder, P extends FilterParam> extends SegmentBuilder<B> {

        private Map<String, Object> filters = new LinkedHashMap<>();

        public Map<String, Object> getFilterParameters() {
            return filters;
        }

        protected void put(String property, Object value) {
            filters.put(property, value);
        }

        @SuppressWarnings("unchecked")
        public T by(P param, Object value) {
            put(param.parameter(), value);
            return (T) this;
        }
    }

    public abstract static class RangeBuilder<T extends RangeBuilder, B extends Builder, P extends RangeParam>
            extends SegmentBuilder<B> {

        private Set<RangeItem> ranges = new LinkedHashSet<>();

        @SuppressWarnings("unchecked")
        public T by(P param, Object value, RangeOperator operator) {
            return (T) new RangeItemBuilder<>(param.parameter(), value, this).withOperator(operator);
        }

        @SuppressWarnings("unchecked")
        protected RangeItemBuilder<T> by(P param, Object value) {
            return (RangeItemBuilder<T>) new RangeItemBuilder<>(param.parameter(), value, this);
        }

        public Map<String, Object> getRangeParameters() {
            Map<String, Object> rangeParameters = new LinkedHashMap<>();
            for (RangeItem range : ranges) {
                rangeParameters.put(getRangeKey(range), range.getValue());
            }
            return rangeParameters;
        }

        void add(RangeItemBuilder<T> rangeItemBuilder) {
            ranges.add(new RangeItem(rangeItemBuilder.name, rangeItemBuilder.value, rangeItemBuilder.operator));
        }

        private String getRangeKey(RangeItem rangeItem) {
            return rangeItem.name + "_" + rangeItem.operator.name();
        }

        public class RangeItemBuilder<R extends RangeBuilder> {

            private final String name;
            private final Object value;
            private RangeOperator operator;

            private final R rangeBuilder;

            RangeItemBuilder(String name, Object value, R rangeBuilder) {
                this.name = name;
                this.rangeBuilder = rangeBuilder;
                this.value = value;
            }

            @SuppressWarnings("unchecked")
            public R gt() {
                operator = RangeOperator.gt;
                rangeBuilder.add(this);
                return rangeBuilder;
            }

            @SuppressWarnings("unchecked")
            public R gte() {
                operator = RangeOperator.gte;
                rangeBuilder.add(this);
                return rangeBuilder;
            }

            @SuppressWarnings("unchecked")
            public R lt() {
                operator = RangeOperator.lt;
                rangeBuilder.add(this);
                return rangeBuilder;
            }

            @SuppressWarnings("unchecked")
            public R lte() {
                operator = RangeOperator.lte;
                rangeBuilder.add(this);
                return rangeBuilder;
            }

            @SuppressWarnings("unchecked")
            public R withOperator(RangeOperator operator) {
                this.operator = operator;
                rangeBuilder.add(this);
                return rangeBuilder;
            }
        }

        private class RangeItem {

            private final String name;
            private final Object value;
            private final RangeOperator operator;

            RangeItem(String name, Object value, RangeOperator operator) {
                this.name = name;
                this.value = value;
                this.operator = operator;
            }

            public String getName() {
                return name;
            }

            public Object getValue() {
                return value;
            }

            public RangeOperator getOperator() {
                return operator;
            }

            @Override public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                @SuppressWarnings("unchecked") RangeItem rangeItem = (RangeItem) o;
                return Objects.equals(name, rangeItem.name)
                        && Objects.equals(value, rangeItem.value)
                        && operator == rangeItem.operator;
            }

            @Override public int hashCode() {
                return Objects.hash(name, value, operator);
            }
        }
    }

    public abstract static class SortBuilder<T extends SortBuilder, B extends Builder, P extends SortParam>
            extends SegmentBuilder<B> {

        private Map<String, SortItem<T>> sorts = new LinkedHashMap<>();

        @SuppressWarnings("unchecked")
        public T by(P param, SortDirection direction) {
            return put(param.parameter(), new SortItem<>((T) this)).direction(direction);
        }

        @SuppressWarnings("unchecked")
        public SortItem<T> by(P param) {
            return put(param.parameter(), new SortItem<>((T) this));
        }

        public String getSortParameter() {
            if (!sorts.isEmpty()) {
                StringBuilder sb = new StringBuilder();

                for (Map.Entry<String, SortItem<T>> sort : sorts.entrySet()) {
                    if (sb.length() > 0) {
                        sb.append(',');
                    }
                    sb.append(getSortProperty(sort.getKey(), sort.getValue().isAscending()));
                }

                return sb.toString();
            }

            return null;
        }

        protected SortItem<T> put(String property, SortItem<T> sortItem) {
            sorts.put(property, sortItem);
            return sortItem;
        }

        private String getSortProperty(String property, boolean ascending) {
            return ascending ? property : "-" + property;
        }

        public class SortItem<S extends SortBuilder> {

            private final S sortBuilder;
            private boolean ascending;

            SortItem(S sortBuilder) {
                this.sortBuilder = sortBuilder;
            }

            public S asc() {
                ascending = true;
                return sortBuilder;
            }

            public S desc() {
                ascending = false;
                return sortBuilder;
            }

            public boolean isAscending() {
                return ascending;
            }

            public S direction(SortDirection direction) {
                ascending = direction == SortDirection.asc;
                return sortBuilder;
            }
        }
    }
}
