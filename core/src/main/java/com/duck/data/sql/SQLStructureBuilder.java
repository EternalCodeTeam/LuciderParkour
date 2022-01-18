package com.duck.data.sql;

public class SQLStructureBuilder {
    private String resultedString;

    public SQLStructureBuilder(String resultedString){
        this.resultedString = resultedString;
    }

    public String getResultedString() {
        return resultedString;
    }

    public void setResultedString(String resultedString) {
        this.resultedString = resultedString;
    }

    public static class StructureBuilder{
        private String resultedString;
        private StringBuilder stringBuilder;

        public StructureBuilder update(String table){
            stringBuilder
                .append("UPDATE ");

            return this;
        }

        public StructureBuilder set(String column, Object value){
            stringBuilder
                .append("SET ")
                .append(column)
                .append(" = ")
                .append(value);


            return this;
        }

        public StructureBuilder select(String column){
            stringBuilder
                .append("SELECT ")
                .append(column);

            return this;
        }

        public StructureBuilder select(String... columns){
            stringBuilder
                .append("SELECT ")
                .append(String.join(", ", columns));

            return this;
        }

        public StructureBuilder selectAsDistinct(String column){
            stringBuilder
                .append("SELECT DISTINCT ")
                .append(column);

            return this;
        }

        public StructureBuilder selectAsDistinct(String... columns){
            stringBuilder
                .append("SELECT DISTINCT ")
                .append(String.join(", ", columns));

            return this;
        }

        public StructureBuilder from(String table){
            stringBuilder
                .append(table)
                .append(" ");

            return this;
        }

        public StructureBuilder where(String condition){
            stringBuilder
                .append(condition);

            return this;
        }

        public String build(){
            return stringBuilder.toString();
        }
    }
}
