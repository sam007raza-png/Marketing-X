package com.kashifmoto.app
import android.app.*
import android.os.Bundle
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.*
import android.widget.*

data class Dish(val name:String,val price:Int,val icon:String)
data class Restaurant(val name:String,val cuisine:String,val rating:Double,val eta:String,val icon:String,val dishes:List<Dish>)

class MainActivity : Activity() {
    private val restaurants = listOf(
        Restaurant("Spice Route","North Indian • Mughlai",4.7,"25–30 min","🍛",listOf(Dish("Butter Chicken",249,"🍗"),Dish("Paneer Tikka",199,"🧀"),Dish("Dal Makhani",169,"🥘"),Dish("Garlic Naan",59,"🫓"),Dish("Biryani",229,"🍚"))),
        Restaurant("Delhi Dastarkhwan","Biryani • Indian",4.6,"30–35 min","🍲",listOf(Dish("Chicken Biryani",239,"🍗"),Dish("Mutton Biryani",299,"🥩"),Dish("Veg Biryani",179,"🍚"),Dish("Raita",49,"🥣"),Dish("Gulab Jamun",79,"🍮"))),
        Restaurant("Tandoori Tales","Tandoor • Kebabs",4.8,"20–25 min","🍢",listOf(Dish("Chicken Tikka",219,"🍢"),Dish("Seekh Kebab",249,"🥩"),Dish("Paneer Tikka",189,"🧀"),Dish("Roomali Roti",39,"🫓"),Dish("Mango Lassi",89,"🥭"))),
        Restaurant("Curry House","Indian • Curry",4.4,"25–30 min","🍲",listOf(Dish("Kadhai Paneer",199,"🧀"),Dish("Butter Chicken",239,"🍗"),Dish("Chole",139,"🥘"),Dish("Jeera Rice",109,"🍚"),Dish("Tandoori Roti",29,"🫓"))),
        Restaurant("Pizza Junction","Pizza • Italian",4.5,"30–40 min","🍕",listOf(Dish("Farmhouse Pizza",299,"🍕"),Dish("Margherita",249,"🍕"),Dish("Chicken Pizza",329,"🍕"),Dish("Garlic Bread",129,"🥖"),Dish("Cold Drink",59,"🥤"))),
        Restaurant("Burger Lab","Burgers • Fast Food",4.3,"20–25 min","🍔",listOf(Dish("Classic Burger",149,"🍔"),Dish("Chicken Burger",179,"🍔"),Dish("Cheese Fries",129,"🍟"),Dish("Wrap",159,"🌯"),Dish("Shake",119,"🥤"))),
        Restaurant("South Spice","South Indian • Dosa",4.7,"20–30 min","🥞",listOf(Dish("Masala Dosa",139,"🥞"),Dish("Idli Sambar",99,"🍘"),Dish("Vada",89,"🍩"),Dish("Paneer Dosa",179,"🥞"),Dish("Filter Coffee",69,"☕"))),
        Restaurant("Chai & Co.","Cafe • Beverages",4.6,"15–20 min","☕",listOf(Dish("Masala Chai",49,"☕"),Dish("Cold Coffee",119,"🥤"),Dish("Veg Sandwich",129,"🥪"),Dish("Brownie",99,"🍫"),Dish("Momos",139,"🥟"))),
        Restaurant("Sweet Truth","Desserts • Bakery",4.5,"25–35 min","🍰",listOf(Dish("Chocolate Cake",179,"🍰"),Dish("Red Velvet",199,"🍰"),Dish("Brownie",129,"🍫"),Dish("Cheesecake",229,"🍰"),Dish("Ice Cream",99,"🍨"))),
        Restaurant("Healthy Bowl","Healthy • Salads",4.4,"20–30 min","🥗",listOf(Dish("Protein Bowl",249,"🥗"),Dish("Paneer Salad",199,"🥗"),Dish("Chicken Bowl",279,"🍗"),Dish("Fruit Bowl",149,"🍓"),Dish("Fresh Juice",99,"🧃")))
    )
    private val cart = linkedMapOf<Dish,Int>()
    private lateinit var root:LinearLayout
    private lateinit var content:LinearLayout
    private var search=""
    private fun dp(v:Int)= (v*resources.displayMetrics.density).toInt()
    private fun tv(s:String,size:Float=14f,bold:Boolean=false)=TextView(this).apply{text=s;textSize=size;setTextColor(Color.rgb(35,35,35));if(bold)setTypeface(typeface,1);setPadding(dp(2),dp(2),dp(2),dp(2))}
    private fun bg(c:Int,r:Int=16)=GradientDrawable().apply{setColor(c);cornerRadius=dp(r).toFloat()}
    private fun btn(s:String,action:()->Unit)=Button(this).apply{text=s;setTextColor(Color.WHITE);background=bg(Color.rgb(232,93,4),14);setOnClickListener{action()}}
    override fun onCreate(b:Bundle?){super.onCreate(b);home()}
    private fun base(title:String){
      root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setBackgroundColor(Color.rgb(250,250,250))}
      val h=LinearLayout(this).apply{orientation=LinearLayout.HORIZONTAL;gravity=Gravity.CENTER_VERTICAL;setPadding(dp(18),dp(10),dp(18),dp(6))}
      h.addView(tv(title,24f,true),LinearLayout.LayoutParams(0,dp(55),1f))
      h.addView(tv("🛒 "+cart.values.sum(),16f,true).apply{gravity=Gravity.CENTER;setOnClickListener{cartScreen()}},LinearLayout.LayoutParams(dp(75),dp(45)))
      root.addView(h)
      content=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(dp(16),0,dp(16),dp(8))}
      root.addView(ScrollView(this).apply{addView(content)},LinearLayout.LayoutParams(-1,0,1f))
      val nav=LinearLayout(this).apply{orientation=LinearLayout.HORIZONTAL;setPadding(dp(8),dp(5),dp(8),dp(5));setBackgroundColor(Color.WHITE)}
      val labels=listOf("⌂ Home","🔎 Search","🛒 Cart","👤 Profile")
      labels.forEach{lab->nav.addView(tv(lab,13f,true).apply{gravity=Gravity.CENTER;setOnClickListener{when(lab.first()){'⌂'->home();'🔎'->searchScreen();'🛒'->cartScreen();else->profile()}}},LinearLayout.LayoutParams(0,dp(52),1f))}
      root.addView(nav);setContentView(root)
    }
    private fun home(){base("KashifMoto 🍽️");val e=EditText(this).apply{hint="Search restaurants, dishes...";singleLine=true;setPadding(dp(16),0,dp(16),0);background=bg(Color.WHITE,24)};content.addView(e,LinearLayout.LayoutParams(-1,dp(52)));content.addView(tv("Good food, delivered fast ⚡",18f,true).apply{setPadding(0,dp(16),0,dp(8))});render(e)}
    private fun render(e:EditText){
      val q=search.lowercase();val data=restaurants.filter{q.isBlank()||it.name.lowercase().contains(q)||it.cuisine.lowercase().contains(q)||it.dishes.any{d->d.name.lowercase().contains(q)}}
      content.addView(tv("Nearby Restaurants",20f,true));data.forEach{restaurantCard(it)}
      content.addView(tv("Popular Dishes",20f,true).apply{setPadding(0,dp(18),0,dp(6))});data.flatMap{r->r.dishes.map{d->d to r}}.take(10).forEach{dishCard(it.first,it.second)}
      e.setOnEditorActionListener{_,_,_->search=e.text.toString();home();true}
    }
    private fun restaurantCard(r:Restaurant){val box=LinearLayout(this).apply{orientation=LinearLayout.HORIZONTAL;gravity=Gravity.CENTER_VERTICAL;setPadding(dp(12),dp(8),dp(8),dp(8));background=bg(Color.WHITE,18);setOnClickListener{restaurant(r)}};box.addView(tv(r.icon,38f).apply{gravity=Gravity.CENTER},LinearLayout.LayoutParams(dp(70),dp(82)));val i=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL};i.addView(tv(r.name,17f,true));i.addView(tv(r.cuisine,13f));i.addView(tv("⭐ "+r.rating+"   •   "+r.eta,13f));box.addView(i,LinearLayout.LayoutParams(0,dp(82),1f));content.addView(box,LinearLayout.LayoutParams(-1,dp(100)).apply{setMargins(0,dp(8),0,0)})}
    private fun dishCard(d:Dish,r:Restaurant){val box=LinearLayout(this).apply{orientation=LinearLayout.HORIZONTAL;gravity=Gravity.CENTER_VERTICAL;setPadding(dp(10),dp(8),dp(8),dp(8));background=bg(Color.WHITE,16)};box.addView(tv(d.icon,32f).apply{gravity=Gravity.CENTER},LinearLayout.LayoutParams(dp(55),dp(62)));val i=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL};i.addView(tv(d.name,15f,true));i.addView(tv("₹"+d.price+" • "+r.name,13f));box.addView(i,LinearLayout.LayoutParams(0,dp(62),1f));box.addView(btn("ADD"){cart[d]=(cart[d]?:0)+1;Toast.makeText(this,"Added to cart",Toast.LENGTH_SHORT).show()},LinearLayout.LayoutParams(dp(72),dp(42)));content.addView(box,LinearLayout.LayoutParams(-1,dp(78)).apply{setMargins(0,dp(6),0,0)})}
    private fun restaurant(r:Restaurant){base(r.name);content.addView(tv(r.cuisine+"  •  ⭐ "+r.rating+"  •  "+r.eta,14f));content.addView(tv("Menu",20f,true).apply{setPadding(0,dp(16),0,dp(5))});r.dishes.forEach{dishCard(it,r)}}
    private fun searchScreen(){base("Search & Filters");val e=EditText(this).apply{hint="Type dish or restaurant";singleLine=true};content.addView(e,LinearLayout.LayoutParams(-1,dp(52)));content.addView(btn("Search"){search=e.text.toString();home()},LinearLayout.LayoutParams(-1,dp(52)).apply{setMargins(0,dp(10),0,0)});content.addView(tv("Filters: Veg • Non-Veg • Under ₹200 • 4.5+ rating",13f).apply{setPadding(0,dp(16),0,dp(8))})}
    private fun cartScreen(){base("Your Cart 🛒");if(cart.isEmpty()){content.addView(tv("Your cart is empty 😋",20f,true));content.addView(btn("Browse Restaurants"){home()});return};var total=0;cart.forEach{(d,q)->total+=d.price*q;val row=LinearLayout(this).apply{orientation=LinearLayout.HORIZONTAL;gravity=Gravity.CENTER_VERTICAL;setPadding(dp(10),0,dp(10),0);background=bg(Color.WHITE,14)};row.addView(tv(d.icon+"  "+d.name,15f,true),LinearLayout.LayoutParams(0,dp(58),1f));row.addView(tv("₹"+(d.price*q)+" ×"+q,14f));content.addView(row,LinearLayout.LayoutParams(-1,dp(68)).apply{setMargins(0,dp(5),0,0)})};content.addView(tv("Total: ₹"+total,22f,true).apply{setPadding(0,dp(18),0,dp(10))});content.addView(btn("Proceed to Checkout"){checkout(total)},LinearLayout.LayoutParams(-1,dp(54)))}
    private fun checkout(total:Int){base("Checkout");content.addView(tv("Order total  ₹"+total,22f,true));content.addView(tv("Delivery address",16f,true).apply{setPadding(0,dp(18),0,dp(7))});val a=EditText(this).apply{hint="Enter delivery address";minLines=3;background=bg(Color.WHITE,14);setPadding(dp(12),dp(10),dp(12),dp(10))};content.addView(a);content.addView(tv("Payment",16f,true).apply{setPadding(0,dp(16),0,dp(7))});val p=RadioGroup(this);p.addView(RadioButton(this).apply{text="Cash on Delivery";isChecked=true});p.addView(RadioButton(this).apply{text="Demo UPI / Razorpay Sandbox"});content.addView(p);content.addView(btn("Place Order • ₹"+total){cart.clear();Toast.makeText(this,"Order placed! Demo payment successful 🎉",Toast.LENGTH_LONG).show();home()},LinearLayout.LayoutParams(-1,dp(54)).apply{setMargins(0,dp(18),0,0)})}
    private fun profile(){base("Profile 👤");content.addView(tv("Welcome to KashifMoto",22f,true));content.addView(tv("Demo account • Login options",14f));content.addView(btn("Login / Sign up"){login()},LinearLayout.LayoutParams(-1,dp(54)).apply{setMargins(0,dp(18),0,0)});content.addView(tv("Google • Email • Phone OTP (demo)",13f).apply{setPadding(0,dp(14),0,dp(8))})}
    private fun login(){base("Login / Sign up");val e=EditText(this).apply{hint="Email or phone";singleLine=true};content.addView(e,LinearLayout.LayoutParams(-1,dp(52)));content.addView(btn("Continue with Google (Demo)"){Toast.makeText(this,"Google demo login successful",Toast.LENGTH_SHORT).show();home()},LinearLayout.LayoutParams(-1,dp(52)).apply{setMargins(0,dp(10),0,0)});content.addView(btn("Send OTP (Demo)"){Toast.makeText(this,"OTP: 123456 (demo)",Toast.LENGTH_SHORT).show()},LinearLayout.LayoutParams(-1,dp(52)).apply{setMargins(0,dp(10),0,0)});content.addView(btn("Continue with Email"){Toast.makeText(this,"Email demo login successful",Toast.LENGTH_SHORT).show();home()},LinearLayout.LayoutParams(-1,dp(52)).apply{setMargins(0,dp(10),0,0)})}
}