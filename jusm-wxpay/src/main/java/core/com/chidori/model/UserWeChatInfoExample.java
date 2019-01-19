package core.com.chidori.model;

import java.util.ArrayList;
import java.util.List;

public class UserWeChatInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserWeChatInfoExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Integer value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Integer value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Integer value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Integer value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Integer value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Integer> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Integer> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Integer value1, Integer value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Integer value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Integer value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Integer value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Integer value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Integer value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Integer> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Integer> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Integer value1, Integer value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUserGidIsNull() {
            addCriterion("user_gid is null");
            return (Criteria) this;
        }

        public Criteria andUserGidIsNotNull() {
            addCriterion("user_gid is not null");
            return (Criteria) this;
        }

        public Criteria andUserGidEqualTo(String value) {
            addCriterion("user_gid =", value, "userGid");
            return (Criteria) this;
        }

        public Criteria andUserGidNotEqualTo(String value) {
            addCriterion("user_gid <>", value, "userGid");
            return (Criteria) this;
        }

        public Criteria andUserGidGreaterThan(String value) {
            addCriterion("user_gid >", value, "userGid");
            return (Criteria) this;
        }

        public Criteria andUserGidGreaterThanOrEqualTo(String value) {
            addCriterion("user_gid >=", value, "userGid");
            return (Criteria) this;
        }

        public Criteria andUserGidLessThan(String value) {
            addCriterion("user_gid <", value, "userGid");
            return (Criteria) this;
        }

        public Criteria andUserGidLessThanOrEqualTo(String value) {
            addCriterion("user_gid <=", value, "userGid");
            return (Criteria) this;
        }

        public Criteria andUserGidLike(String value) {
            addCriterion("user_gid like", value, "userGid");
            return (Criteria) this;
        }

        public Criteria andUserGidNotLike(String value) {
            addCriterion("user_gid not like", value, "userGid");
            return (Criteria) this;
        }

        public Criteria andUserGidIn(List<String> values) {
            addCriterion("user_gid in", values, "userGid");
            return (Criteria) this;
        }

        public Criteria andUserGidNotIn(List<String> values) {
            addCriterion("user_gid not in", values, "userGid");
            return (Criteria) this;
        }

        public Criteria andUserGidBetween(String value1, String value2) {
            addCriterion("user_gid between", value1, value2, "userGid");
            return (Criteria) this;
        }

        public Criteria andUserGidNotBetween(String value1, String value2) {
            addCriterion("user_gid not between", value1, value2, "userGid");
            return (Criteria) this;
        }

        public Criteria andOpenIdIsNull() {
            addCriterion("open_id is null");
            return (Criteria) this;
        }

        public Criteria andOpenIdIsNotNull() {
            addCriterion("open_id is not null");
            return (Criteria) this;
        }

        public Criteria andOpenIdEqualTo(String value) {
            addCriterion("open_id =", value, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdNotEqualTo(String value) {
            addCriterion("open_id <>", value, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdGreaterThan(String value) {
            addCriterion("open_id >", value, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdGreaterThanOrEqualTo(String value) {
            addCriterion("open_id >=", value, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdLessThan(String value) {
            addCriterion("open_id <", value, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdLessThanOrEqualTo(String value) {
            addCriterion("open_id <=", value, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdLike(String value) {
            addCriterion("open_id like", value, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdNotLike(String value) {
            addCriterion("open_id not like", value, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdIn(List<String> values) {
            addCriterion("open_id in", values, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdNotIn(List<String> values) {
            addCriterion("open_id not in", values, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdBetween(String value1, String value2) {
            addCriterion("open_id between", value1, value2, "openId");
            return (Criteria) this;
        }

        public Criteria andOpenIdNotBetween(String value1, String value2) {
            addCriterion("open_id not between", value1, value2, "openId");
            return (Criteria) this;
        }

        public Criteria andUnionIdIsNull() {
            addCriterion("union_id is null");
            return (Criteria) this;
        }

        public Criteria andUnionIdIsNotNull() {
            addCriterion("union_id is not null");
            return (Criteria) this;
        }

        public Criteria andUnionIdEqualTo(String value) {
            addCriterion("union_id =", value, "unionId");
            return (Criteria) this;
        }

        public Criteria andUnionIdNotEqualTo(String value) {
            addCriterion("union_id <>", value, "unionId");
            return (Criteria) this;
        }

        public Criteria andUnionIdGreaterThan(String value) {
            addCriterion("union_id >", value, "unionId");
            return (Criteria) this;
        }

        public Criteria andUnionIdGreaterThanOrEqualTo(String value) {
            addCriterion("union_id >=", value, "unionId");
            return (Criteria) this;
        }

        public Criteria andUnionIdLessThan(String value) {
            addCriterion("union_id <", value, "unionId");
            return (Criteria) this;
        }

        public Criteria andUnionIdLessThanOrEqualTo(String value) {
            addCriterion("union_id <=", value, "unionId");
            return (Criteria) this;
        }

        public Criteria andUnionIdLike(String value) {
            addCriterion("union_id like", value, "unionId");
            return (Criteria) this;
        }

        public Criteria andUnionIdNotLike(String value) {
            addCriterion("union_id not like", value, "unionId");
            return (Criteria) this;
        }

        public Criteria andUnionIdIn(List<String> values) {
            addCriterion("union_id in", values, "unionId");
            return (Criteria) this;
        }

        public Criteria andUnionIdNotIn(List<String> values) {
            addCriterion("union_id not in", values, "unionId");
            return (Criteria) this;
        }

        public Criteria andUnionIdBetween(String value1, String value2) {
            addCriterion("union_id between", value1, value2, "unionId");
            return (Criteria) this;
        }

        public Criteria andUnionIdNotBetween(String value1, String value2) {
            addCriterion("union_id not between", value1, value2, "unionId");
            return (Criteria) this;
        }

        public Criteria andSubscribeStatusIsNull() {
            addCriterion("subscribe_status is null");
            return (Criteria) this;
        }

        public Criteria andSubscribeStatusIsNotNull() {
            addCriterion("subscribe_status is not null");
            return (Criteria) this;
        }

        public Criteria andSubscribeStatusEqualTo(Integer value) {
            addCriterion("subscribe_status =", value, "subscribeStatus");
            return (Criteria) this;
        }

        public Criteria andSubscribeStatusNotEqualTo(Integer value) {
            addCriterion("subscribe_status <>", value, "subscribeStatus");
            return (Criteria) this;
        }

        public Criteria andSubscribeStatusGreaterThan(Integer value) {
            addCriterion("subscribe_status >", value, "subscribeStatus");
            return (Criteria) this;
        }

        public Criteria andSubscribeStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("subscribe_status >=", value, "subscribeStatus");
            return (Criteria) this;
        }

        public Criteria andSubscribeStatusLessThan(Integer value) {
            addCriterion("subscribe_status <", value, "subscribeStatus");
            return (Criteria) this;
        }

        public Criteria andSubscribeStatusLessThanOrEqualTo(Integer value) {
            addCriterion("subscribe_status <=", value, "subscribeStatus");
            return (Criteria) this;
        }

        public Criteria andSubscribeStatusIn(List<Integer> values) {
            addCriterion("subscribe_status in", values, "subscribeStatus");
            return (Criteria) this;
        }

        public Criteria andSubscribeStatusNotIn(List<Integer> values) {
            addCriterion("subscribe_status not in", values, "subscribeStatus");
            return (Criteria) this;
        }

        public Criteria andSubscribeStatusBetween(Integer value1, Integer value2) {
            addCriterion("subscribe_status between", value1, value2, "subscribeStatus");
            return (Criteria) this;
        }

        public Criteria andSubscribeStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("subscribe_status not between", value1, value2, "subscribeStatus");
            return (Criteria) this;
        }

        public Criteria andSubscribeWayIsNull() {
            addCriterion("subscribe_way is null");
            return (Criteria) this;
        }

        public Criteria andSubscribeWayIsNotNull() {
            addCriterion("subscribe_way is not null");
            return (Criteria) this;
        }

        public Criteria andSubscribeWayEqualTo(Integer value) {
            addCriterion("subscribe_way =", value, "subscribeWay");
            return (Criteria) this;
        }

        public Criteria andSubscribeWayNotEqualTo(Integer value) {
            addCriterion("subscribe_way <>", value, "subscribeWay");
            return (Criteria) this;
        }

        public Criteria andSubscribeWayGreaterThan(Integer value) {
            addCriterion("subscribe_way >", value, "subscribeWay");
            return (Criteria) this;
        }

        public Criteria andSubscribeWayGreaterThanOrEqualTo(Integer value) {
            addCriterion("subscribe_way >=", value, "subscribeWay");
            return (Criteria) this;
        }

        public Criteria andSubscribeWayLessThan(Integer value) {
            addCriterion("subscribe_way <", value, "subscribeWay");
            return (Criteria) this;
        }

        public Criteria andSubscribeWayLessThanOrEqualTo(Integer value) {
            addCriterion("subscribe_way <=", value, "subscribeWay");
            return (Criteria) this;
        }

        public Criteria andSubscribeWayIn(List<Integer> values) {
            addCriterion("subscribe_way in", values, "subscribeWay");
            return (Criteria) this;
        }

        public Criteria andSubscribeWayNotIn(List<Integer> values) {
            addCriterion("subscribe_way not in", values, "subscribeWay");
            return (Criteria) this;
        }

        public Criteria andSubscribeWayBetween(Integer value1, Integer value2) {
            addCriterion("subscribe_way between", value1, value2, "subscribeWay");
            return (Criteria) this;
        }

        public Criteria andSubscribeWayNotBetween(Integer value1, Integer value2) {
            addCriterion("subscribe_way not between", value1, value2, "subscribeWay");
            return (Criteria) this;
        }

        public Criteria andNickNameIsNull() {
            addCriterion("nick_name is null");
            return (Criteria) this;
        }

        public Criteria andNickNameIsNotNull() {
            addCriterion("nick_name is not null");
            return (Criteria) this;
        }

        public Criteria andNickNameEqualTo(String value) {
            addCriterion("nick_name =", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotEqualTo(String value) {
            addCriterion("nick_name <>", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameGreaterThan(String value) {
            addCriterion("nick_name >", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameGreaterThanOrEqualTo(String value) {
            addCriterion("nick_name >=", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameLessThan(String value) {
            addCriterion("nick_name <", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameLessThanOrEqualTo(String value) {
            addCriterion("nick_name <=", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameLike(String value) {
            addCriterion("nick_name like", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotLike(String value) {
            addCriterion("nick_name not like", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameIn(List<String> values) {
            addCriterion("nick_name in", values, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotIn(List<String> values) {
            addCriterion("nick_name not in", values, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameBetween(String value1, String value2) {
            addCriterion("nick_name between", value1, value2, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotBetween(String value1, String value2) {
            addCriterion("nick_name not between", value1, value2, "nickName");
            return (Criteria) this;
        }

        public Criteria andSexIsNull() {
            addCriterion("sex is null");
            return (Criteria) this;
        }

        public Criteria andSexIsNotNull() {
            addCriterion("sex is not null");
            return (Criteria) this;
        }

        public Criteria andSexEqualTo(Integer value) {
            addCriterion("sex =", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotEqualTo(Integer value) {
            addCriterion("sex <>", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThan(Integer value) {
            addCriterion("sex >", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThanOrEqualTo(Integer value) {
            addCriterion("sex >=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThan(Integer value) {
            addCriterion("sex <", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThanOrEqualTo(Integer value) {
            addCriterion("sex <=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexIn(List<Integer> values) {
            addCriterion("sex in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotIn(List<Integer> values) {
            addCriterion("sex not in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexBetween(Integer value1, Integer value2) {
            addCriterion("sex between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotBetween(Integer value1, Integer value2) {
            addCriterion("sex not between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andCityIsNull() {
            addCriterion("city is null");
            return (Criteria) this;
        }

        public Criteria andCityIsNotNull() {
            addCriterion("city is not null");
            return (Criteria) this;
        }

        public Criteria andCityEqualTo(String value) {
            addCriterion("city =", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotEqualTo(String value) {
            addCriterion("city <>", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThan(String value) {
            addCriterion("city >", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityGreaterThanOrEqualTo(String value) {
            addCriterion("city >=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThan(String value) {
            addCriterion("city <", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLessThanOrEqualTo(String value) {
            addCriterion("city <=", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityLike(String value) {
            addCriterion("city like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotLike(String value) {
            addCriterion("city not like", value, "city");
            return (Criteria) this;
        }

        public Criteria andCityIn(List<String> values) {
            addCriterion("city in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotIn(List<String> values) {
            addCriterion("city not in", values, "city");
            return (Criteria) this;
        }

        public Criteria andCityBetween(String value1, String value2) {
            addCriterion("city between", value1, value2, "city");
            return (Criteria) this;
        }

        public Criteria andCityNotBetween(String value1, String value2) {
            addCriterion("city not between", value1, value2, "city");
            return (Criteria) this;
        }

        public Criteria andProvinceIsNull() {
            addCriterion("province is null");
            return (Criteria) this;
        }

        public Criteria andProvinceIsNotNull() {
            addCriterion("province is not null");
            return (Criteria) this;
        }

        public Criteria andProvinceEqualTo(String value) {
            addCriterion("province =", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotEqualTo(String value) {
            addCriterion("province <>", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThan(String value) {
            addCriterion("province >", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceGreaterThanOrEqualTo(String value) {
            addCriterion("province >=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThan(String value) {
            addCriterion("province <", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLessThanOrEqualTo(String value) {
            addCriterion("province <=", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceLike(String value) {
            addCriterion("province like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotLike(String value) {
            addCriterion("province not like", value, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceIn(List<String> values) {
            addCriterion("province in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotIn(List<String> values) {
            addCriterion("province not in", values, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceBetween(String value1, String value2) {
            addCriterion("province between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andProvinceNotBetween(String value1, String value2) {
            addCriterion("province not between", value1, value2, "province");
            return (Criteria) this;
        }

        public Criteria andCountryIsNull() {
            addCriterion("country is null");
            return (Criteria) this;
        }

        public Criteria andCountryIsNotNull() {
            addCriterion("country is not null");
            return (Criteria) this;
        }

        public Criteria andCountryEqualTo(String value) {
            addCriterion("country =", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryNotEqualTo(String value) {
            addCriterion("country <>", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryGreaterThan(String value) {
            addCriterion("country >", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryGreaterThanOrEqualTo(String value) {
            addCriterion("country >=", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryLessThan(String value) {
            addCriterion("country <", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryLessThanOrEqualTo(String value) {
            addCriterion("country <=", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryLike(String value) {
            addCriterion("country like", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryNotLike(String value) {
            addCriterion("country not like", value, "country");
            return (Criteria) this;
        }

        public Criteria andCountryIn(List<String> values) {
            addCriterion("country in", values, "country");
            return (Criteria) this;
        }

        public Criteria andCountryNotIn(List<String> values) {
            addCriterion("country not in", values, "country");
            return (Criteria) this;
        }

        public Criteria andCountryBetween(String value1, String value2) {
            addCriterion("country between", value1, value2, "country");
            return (Criteria) this;
        }

        public Criteria andCountryNotBetween(String value1, String value2) {
            addCriterion("country not between", value1, value2, "country");
            return (Criteria) this;
        }

        public Criteria andHeadImgUrlIsNull() {
            addCriterion("head_img_url is null");
            return (Criteria) this;
        }

        public Criteria andHeadImgUrlIsNotNull() {
            addCriterion("head_img_url is not null");
            return (Criteria) this;
        }

        public Criteria andHeadImgUrlEqualTo(String value) {
            addCriterion("head_img_url =", value, "headImgUrl");
            return (Criteria) this;
        }

        public Criteria andHeadImgUrlNotEqualTo(String value) {
            addCriterion("head_img_url <>", value, "headImgUrl");
            return (Criteria) this;
        }

        public Criteria andHeadImgUrlGreaterThan(String value) {
            addCriterion("head_img_url >", value, "headImgUrl");
            return (Criteria) this;
        }

        public Criteria andHeadImgUrlGreaterThanOrEqualTo(String value) {
            addCriterion("head_img_url >=", value, "headImgUrl");
            return (Criteria) this;
        }

        public Criteria andHeadImgUrlLessThan(String value) {
            addCriterion("head_img_url <", value, "headImgUrl");
            return (Criteria) this;
        }

        public Criteria andHeadImgUrlLessThanOrEqualTo(String value) {
            addCriterion("head_img_url <=", value, "headImgUrl");
            return (Criteria) this;
        }

        public Criteria andHeadImgUrlLike(String value) {
            addCriterion("head_img_url like", value, "headImgUrl");
            return (Criteria) this;
        }

        public Criteria andHeadImgUrlNotLike(String value) {
            addCriterion("head_img_url not like", value, "headImgUrl");
            return (Criteria) this;
        }

        public Criteria andHeadImgUrlIn(List<String> values) {
            addCriterion("head_img_url in", values, "headImgUrl");
            return (Criteria) this;
        }

        public Criteria andHeadImgUrlNotIn(List<String> values) {
            addCriterion("head_img_url not in", values, "headImgUrl");
            return (Criteria) this;
        }

        public Criteria andHeadImgUrlBetween(String value1, String value2) {
            addCriterion("head_img_url between", value1, value2, "headImgUrl");
            return (Criteria) this;
        }

        public Criteria andHeadImgUrlNotBetween(String value1, String value2) {
            addCriterion("head_img_url not between", value1, value2, "headImgUrl");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}