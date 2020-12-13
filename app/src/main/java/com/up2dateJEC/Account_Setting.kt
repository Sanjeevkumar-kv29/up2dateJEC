package com.up2dateJEC

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.up2dateJEC.Login_Activity
import com.example.up2dateJEC.MainActivity
import com.example.up2dateJEC.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.up2dateJEC.Adapter.MyAchivementListAdapter
import kotlinx.android.synthetic.main.activity_accountsetting.*

private var firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private var fsdb: FirebaseFirestore? = FirebaseFirestore.getInstance()
    private var pD: ProgressDialog? = null

class Account_Setting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accountsetting)

        pD = ProgressDialog(this)



        viewprofile.setOnClickListener {

            upll2.visibility = View.VISIBLE
            viewprofilelayout.visibility = View.VISIBLE
            viewachievementlayout.visibility = View.VISIBLE
            changepasswordlayout.visibility = View.GONE
            deleteuseraccountlayout.visibility = View.GONE
            actiontv.setText("Your Profile Details")
            ASbutton1.visibility = View.VISIBLE
            ASbutton2.visibility = View.VISIBLE
            ASbutton1.setText("Go Back")
            ASbutton2.setText("Update Profile")
            useremailmtv.isEnabled = false
            val currentloguser = firebaseAuth?.currentUser
            val currentuseremail = currentloguser?.email.toString()

            pD?.setMessage("Please Wait ")
            pD?.show()

            viewProfile(currentuseremail)
        }

        viewachievement.setOnClickListener {


            upll2.visibility = View.VISIBLE
            viewprofilelayout.visibility = View.GONE
            viewachievementlayout.visibility = View.VISIBLE
            changepasswordlayout.visibility = View.GONE
            deleteuseraccountlayout.visibility = View.GONE
            actiontv.setText("Your Achievement Details")
            ASbutton1.visibility = View.VISIBLE
            ASbutton1.setText("Go Back")
            ASbutton2.visibility = View.GONE



            val language = arrayOf<String>("C language certified","C++ language certified","Java language certified","Kotlin language certified","Python language certified","Php language certified","Android language certified")
            val description = arrayOf<String>(
                "C programming is considered as the base for other programming languages",
                "C++ is an object-oriented programming language.",
                "Java is a programming language and a platform.",
                "Kotlin is a open-source programming language, used to develop Android apps and much more.",
                "Python is interpreted scripting  and object-oriented programming language.",
                "PHP is an interpreted language, i.e., there is no need for compilation.",
                "Android is an open source framework from Apache written in Java."
            )

            val imageId = arrayOf<Int>(
                R.drawable.pathshala,R.drawable.pathshala,R.drawable.pathshala,R.drawable.pathshala,R.drawable.pathshala,
                R.drawable.pathshala,R.drawable.pathshala
            )


            val myListAdapter = MyAchivementListAdapter(this,language,description,imageId)
            achievementListView.adapter = myListAdapter

            achievementListView.setOnItemClickListener(){adapterView, view, position, id ->
                val itemAtPos = adapterView.getItemAtPosition(position)
                val itemIdAtPos = adapterView.getItemIdAtPosition(position)
                Toast.makeText(this, "Click on item at $itemAtPos its item id $itemIdAtPos", Toast.LENGTH_LONG).show()
            }


        }


        changepassword.setOnClickListener {

            upll2.visibility = View.VISIBLE
            viewachievementlayout.visibility = View.GONE
            viewprofilelayout.visibility = View.GONE
            changepasswordlayout.visibility = View.VISIBLE
            deleteuseraccountlayout.visibility = View.GONE
            actiontv.setText("Change Your Password")
            ASbutton1.visibility = View.VISIBLE
            ASbutton1.setText("Change Password")
            ASbutton2.visibility = View.GONE



        }

        deleteaccount.setOnClickListener {

            upll2.visibility = View.VISIBLE
            viewachievementlayout.visibility = View.GONE
            viewprofilelayout.visibility = View.GONE
            changepasswordlayout.visibility = View.GONE
            deleteuseraccountlayout.visibility = View.VISIBLE
            actiontv.setText("Permanently Delete Your Account")
            ASbutton1.visibility = View.VISIBLE
            ASbutton1.setText("Delete Account")
            ASbutton2.visibility = View.GONE


        }

        ASbutton1.setOnClickListener {



//-----------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------


            if (ASbutton1.text.toString().equals("Go Back")){

                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(R.anim.enteranim,R.anim.exitanim)


            }




//-----------------------------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------------------------


            if (ASbutton1.text.toString().equals("Change Password")){


                if (changepasswordET.text.isNullOrEmpty()){
                    changepasswordET.setError("This Field should not be empty")
                }
                else if (changepasswordET.text.toString().length<6){
                    changepasswordET.setError("Password Length Must be Grater then 6")
                }
                else if (confirmchangepasswordET.text.isNullOrEmpty()){
                    confirmchangepasswordET.setError("This Field Should not be empty")
                }
                else if (changepasswordET.text.toString().equals(confirmchangepasswordET.text.toString())){
                    pD?.setMessage("Please Wait ")
                    pD?.show()
                    changepass(changepasswordET.text.toString())
                }
                else{
                    changepasswordET.setError("Password and Confirm Password Must Be Same")
                }


            }


 //----------------------------------------------------------------------------------------------------------------------------------------------------------------
 //----------------------------------------------------------------------------------------------------------------------------------------------------------------

            if (ASbutton1.text.toString().equals("Delete Account")){

                val user = firebaseAuth?.currentUser
                var currentuseremail  = user?.getEmail().toString()


                if (useremailidtodelete.text.toString().isNullOrEmpty()){

                    useremailidtodelete.setError("This field Should Not Be Empty")
                }
                else if (!deleteaccountCB.isChecked){

                    deleteaccountCB.setError("This field should be cheaked")


                }
                else if (currentuseremail!=useremailidtodelete.text.toString()){

                    Toast.makeText(this,"email is "+currentuseremail,Toast.LENGTH_SHORT).show()
                    useremailidtodelete.setError("Enter Correct Registered Email ID ")

                }
                else{

                    pD?.setMessage("Please Wait ")
                    pD?.show()

                    deleteuserFSaccount()



                    val dialogBuilder = AlertDialog.Builder(this)
                    dialogBuilder.setMessage("Do you really want to Delete your account from PaathShala ? \n" +
                            "Click Confirm For Proceed Further")

                        .setCancelable(false)
                        .setPositiveButton("Proceed", DialogInterface.OnClickListener {
                                dialog, id -> finish()


                            deleteuserAUTHENTICATIONaccount()

                            val sharepref: SharedPreferences = getSharedPreferences("currentuserdetails",0)
                            val editor: SharedPreferences.Editor = sharepref.edit()
                            editor.clear()
                            editor.apply()

                            Toast.makeText(this,"Account Deleted Successfully",Toast.LENGTH_LONG).show()

                            startActivity(Intent(this, Login_Activity::class.java))


                        })



                        .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                                dialog, id -> dialog.cancel()
                        })

                    val alert = dialogBuilder.create()
                    alert.setTitle("Alert")
                    alert.show()
                }

                }


            }

        ASbutton2.setOnClickListener {


            if (ASbutton2.text.toString().equals("Update Profile")){

                pD?.setMessage("Please Wait ")
                pD?.show()
                updateProfile()

            }


        }


        }



    fun updateProfile()
    {

        var currentloguser = firebaseAuth?.currentUser


        val user: MutableMap<String, Any> = HashMap()
        user["NAME"] = usernamemtv.text.toString()
        user["EMAIL"] = currentloguser?.email.toString()
        user["MOBILENO"] = usermobilemtv.text.toString()
        user["DATE-OF-BIRTH"] = userdobmtv.text.toString()
        user["COLLEGE-NAME"] = usercollegenamemtv.text.toString()
        user["USER-ADDRESS"] = useraddressmtv.text.toString()
        user["LINKEDIN-PROFILE"] = linkedinprofilelink.text.toString()


        fsdb?.collection("REGISTERED-USERS")?.document(currentloguser?.email.toString())
            ?.update(user)
            ?.addOnSuccessListener { documentReference ->
                Log.d("Success","DocumentSnapshot added with ID: " + documentReference)

                Toast.makeText(this,"PROFILE UPDATE SUCCESSFULLY",Toast.LENGTH_SHORT).show()

                pD?.dismiss()
            }
            ?.addOnFailureListener { e -> Log.w("error", "Error adding document", e)

            }


    }



    fun viewProfile(currentuser:String){

        var Fetchedcurrentuser:MutableMap<String, Any> = HashMap()

        val docRef = fsdb?.collection("REGISTERED-USERS")?.document(currentuser)
        docRef?.get()
            ?.addOnSuccessListener { document ->
                if (document.data != null) {

                    Log.d("ViewProfile", "DocumentSnapshot data: ${document.data}")

                    Fetchedcurrentuser.putAll(document.data!!)
                    Log.d("fetched", "DocumentSnapshot data: ${Fetchedcurrentuser}")


                    var username  = Fetchedcurrentuser.get("NAME").toString()
                    //var useremail= Fetchedcurrentuser.get("EMAIL").toString()
                    var usermobile = Fetchedcurrentuser.get("MOBILENO").toString()
                    var userDOB = Fetchedcurrentuser.get("DATE-OF-BIRTH").toString()
                    var userCollegename = Fetchedcurrentuser.get("COLLEGE-NAME").toString()
                    var userAddress = Fetchedcurrentuser.get("USER-ADDRESS").toString()
                    var userlinkedin = Fetchedcurrentuser.get("LINKEDIN-PROFILE").toString()

                    usernamemtv.setText(username)
                    useremailmtv.setText(currentuser)
                    usermobilemtv.setText(usermobile)
                    userdobmtv.setText(userDOB)
                    usercollegenamemtv.setText(userCollegename)
                    useraddressmtv.setText(userAddress)
                    linkedinprofilelink.setText(userlinkedin)

                    pD?.dismiss()


                } else {
                    Log.d("Not EXIST", "No such document")


                }

            }
            ?.addOnFailureListener { exception ->
                Log.d("ERROR OCCURRED","get failed with ", exception)
            }
    }







        fun changepass(newPassword:String){

        val user = firebaseAuth?.currentUser

        user!!.updatePassword(newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    pD?.dismiss()
                    Log.d("Change Pass", "User password updated.")
                    Toast.makeText(this,"Password Changed SuccessFully",Toast.LENGTH_SHORT).show()
                }
            }

    }




    fun deleteuserAUTHENTICATIONaccount(){

        firebaseAuth = FirebaseAuth.getInstance()
        val Cuser = firebaseAuth?.currentUser!!


        firebaseAuth?.signOut()

        Cuser.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    pD?.dismiss()
                    Log.d("Data not deleted", "User account deleted.")

                }
            }

    }


    fun deleteuserFSaccount(){

        firebaseAuth = FirebaseAuth.getInstance()
        val Cuser = firebaseAuth?.currentUser!!

        fsdb?.collection("REGISTERED-USERS")?.document(Cuser.email.toString())
            ?.delete()
            ?.addOnSuccessListener { Log.d("cities", "DocumentSnapshot successfully deleted!")



                Toast.makeText(this,"deleted SuccessFully",Toast.LENGTH_SHORT).show()

            }
            ?.addOnFailureListener { e -> Log.w("cities", "Error deleting document", e) }

    }





}






