package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.PlayList;
import com.example.demo.repositories.PlayListRepository;

@Service
public class PlayListserviceImplementation implements PlayListService{

	@Autowired
	PlayListRepository prepo;

	@Override
	public void addPlaylist(PlayList playlist) {
		prepo.save(playlist);
	}
}
