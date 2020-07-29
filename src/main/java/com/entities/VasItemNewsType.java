package com.entities;

import com.facade.DBConnect;
import com.logger.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VasItemNewsType {
    public int id, parent, status, hasChild;
    public String name, url;

    public static List<VasItemNewsType> getArticleType() {
        List<VasItemNewsType> list = new ArrayList<VasItemNewsType>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (!DBConnect.getConnection().isClosed()) {
                con = DBConnect.getConnection();
                ps = con.prepareStatement("select * from article_type where url is not null AND status = 1 AND parent = 0");
                rs = ps.executeQuery();
                while (rs.next()) {
                    VasItemNewsType v = new VasItemNewsType();
                    v.id = rs.getInt("id");
                    v.name = rs.getString("name");
                    v.parent = rs.getInt("parent");
                    v.status = rs.getInt("status");
                    v.hasChild = rs.getInt("has_child");
                    v.url = rs.getString("url");
                    list.add(v);
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        } finally {
            closeCon(con, ps, rs);
        }
        return list;
    }

    public static VasItemNewsType getArticleTypeById(int id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (!DBConnect.getConnection().isClosed()) {
                System.out.println("start getArticleTypeById getConnection");
                con = DBConnect.getConnection();
                System.out.println("end getArticleTypeById getConnection");
                ps = con.prepareStatement("select * from article_type where url is not null AND status = 1 AND id = ? limit 1");
                ps.setInt(1, id);

                rs = ps.executeQuery();
                while (rs.next()) {
                    VasItemNewsType v = new VasItemNewsType();
                    v.id = rs.getInt("id");
                    v.name = rs.getString("name");
                    v.parent = rs.getInt("parent");
                    v.status = rs.getInt("status");
                    v.hasChild = rs.getInt("has_child");
                    v.url = rs.getString("url");
                    return v;
                }

            }
        } catch (Exception e) {
            Logger.error(e);
        } finally {
            closeCon(con, ps, rs);
        }
        return null;
    }

    private static void closeCon(Connection con, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }
}
