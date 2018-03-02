package com.thomaskioko.livedatademo.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.thomaskioko.livedatademo.db.entity.Movie;
import com.thomaskioko.livedatademo.db.entity.TmdbVideo;
import com.thomaskioko.livedatademo.repository.TmdbRepository;
import com.thomaskioko.livedatademo.utils.AbsentLiveData;
import com.thomaskioko.livedatademo.utils.Objects;
import com.thomaskioko.livedatademo.vo.Resource;

import java.util.List;

import javax.inject.Inject;


public class MovieDetailViewModel extends ViewModel {

    @VisibleForTesting
    final MutableLiveData<Integer> movieId = new MutableLiveData<>();
    private final LiveData<Resource<Movie>> movie;
    private final LiveData<Resource<List<TmdbVideo>>> videos;

    @Inject
    MovieDetailViewModel(TmdbRepository tmdbRepository) {
        movie = Transformations.switchMap(movieId, movieId -> {
            if (movieId == null) {
                return AbsentLiveData.create();
            } else {
                return tmdbRepository.getMovieById(movieId);
            }
        });

        videos = Transformations.switchMap(movieId, movieId -> {
            if (movieId == null) {
                return AbsentLiveData.create();
            } else {
                return tmdbRepository.getMovieVideo(movieId);
            }
        });
    }

    @VisibleForTesting
    public LiveData<Resource<Movie>> getMovie() {
        return movie;
    }

    @VisibleForTesting
    public void setMovieId(int movieId) {
        if (Objects.equals(movieId, this.movieId.getValue())) {
            return;
        }

        this.movieId.setValue(movieId);
    }

    @VisibleForTesting
    public LiveData<Resource<List<TmdbVideo>>> getVideoMovies() {
        return videos;
    }
}
