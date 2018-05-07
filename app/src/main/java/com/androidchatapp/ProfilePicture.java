package com.androidchatapp;

public class ProfilePicture {
    var uploader = document.getElementById('uploader');
    var fileButton = document.getElementById('fileButton');
    var user = firebase.auth().currentUser;

    fileButton.addEventListener('change', function(e){

        //Get File
        var file = e.target.files[0];


        //Create a Storage Ref
        var storageRef = firebase.storage().ref(user + 'profilePictures/' + file.name);

        //Upload file
        var task = storageRef.put(file);

        var user = firebase.auth().currentUser;

        function error(err){
        },
        function complete(){
        }
       );
    }
