package com.soomter;

import java.util.ArrayList;
import java.util.List;

public class GenreDataFactory {

    private static List<Genre> list;

    public static List<Genre> makeGenres(List<ProductCatgsTreeData.Datum> result) {

        list = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            list.add(makeRockGenre(result.get(i)));
        }

        return list;
    }

    public static Genre makeRockGenre(ProductCatgsTreeData.Datum obj) {
        return new Genre(obj.name, obj.Image, makeRockArtists(obj.childs), R.mipmap.ads_icon);
    }

    public static List<Artist> makeRockArtists(List<ProductCatgsTreeData.Childs> child) {
        List<Artist> artists = new ArrayList<>();
        for (int i = 0; i < child.size(); i++) {
            Artist artist = new Artist(child.get(i).name, child.get(i).Image);
            artists.add(artist);
        }
        return artists;
    }


}

