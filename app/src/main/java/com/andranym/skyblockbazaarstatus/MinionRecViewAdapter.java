package com.andranym.skyblockbazaarstatus;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

public class MinionRecViewAdapter extends RecyclerView.Adapter<MinionRecViewAdapter.ViewHolder> {

    private ArrayList<Minion> minions = new ArrayList<>();

    public void setMinions(ArrayList<Minion> minions) {
        this.minions = minions;
        notifyDataSetChanged();
    }

    Context context;

    //Store whether or not a minion is collapsed, make first one expanded, rest collapsed
    //Forgot how many minions there were, no more than this many, that's for sure
    final boolean[] collapsed = {false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,
            true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,
            true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,
            true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,
            true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,
            true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,
            true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,
            true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,
            true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,
            true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,
            true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,
            true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,
            true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,
            true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,
            true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,
            true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
    //This is definitely my favorite bodge of the whole app so far.


    public MinionRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MinionRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_minion_properties,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MinionRecViewAdapter.ViewHolder holder, final int position) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        final int customBoostNormal = settings.getInt("customBoostNormal",0);
        final int customBoostFly = settings.getInt("customBoostFly",0);
        final boolean solved4 = settings.getBoolean("solvedChallenge4",false);

        //region Set initial values based on saved data
        holder.txtProducts.setText(minions.get(position).getProducts());
        holder.txtNPCPrice.setText(minions.get(position).getNPCPrices());
        String title = (position + 1) + ": " + minions.get(position).getTitle();
        holder.txtMinionName.setText(title);
        holder.txtBazaarPrice.setText(minions.get(position).getBazaarPrices());
        holder.txtMinionWarnings.setText(minions.get(position).getWarnings());
        holder.txtTierInfo.setText(minions.get(position).getTierProfits());
        holder.txtItemsPerAction.setText(minions.get(position).getItemsPerAction());
        holder.editTextNumberDecimalPetBoost.setText(Double.toString(minions.get(position).getPetBoost()*100));
        holder.editTextNumberDecimalMiscBoost1.setText(Double.toString(minions.get(position).getMiscBoost1()*100));
        holder.editTextNumberDecimalMiscBoost2.setText(Double.toString(minions.get(position).getMiscBoost2()*100));
        holder.editTextNumberAdditionalMultiplier.setText(Double.toString(minions.get(position).getAdditionalMultiplier()));

        //regionCollapse most to make it look better
        if (collapsed[position]) {
            holder.txtTierInfo.setVisibility(View.GONE);
            holder.productScroll.setVisibility(View.GONE);
            holder.txtMinionWarnings.setVisibility(View.GONE);
            holder.txtFuelRecLabel.setVisibility(View.GONE);
            holder.radioGroup.setVisibility(View.GONE);
            holder.txtRecFuelType.setVisibility(View.GONE);
            holder.spinnerRecUpgrade1.setVisibility(View.GONE);
            holder.spinnerRecUpgrade2.setVisibility(View.GONE);
            holder.txtUpgrade1.setVisibility(View.GONE);
            holder.txtUpgrade2.setVisibility(View.GONE);
            holder.txtPetBoostLabel.setVisibility(View.GONE);
            holder.txtExtraBoost1.setVisibility(View.GONE);
            holder.txtExtraBoost2.setVisibility(View.GONE);
            holder.editTextNumberDecimalMiscBoost1.setVisibility(View.GONE);
            holder.editTextNumberDecimalMiscBoost2.setVisibility(View.GONE);
            holder.editTextNumberRecFuel.setVisibility(View.GONE);
            holder.editTextNumberDecimalPetBoost.setVisibility(View.GONE);
            holder.btnMinionUpdate.setVisibility(View.GONE);
            holder.txtRecMultiplier.setVisibility(View.GONE);
            holder.editTextNumberAdditionalMultiplier.setVisibility(View.GONE);
        }
        if (!solved4) {
            holder.txtRecMultiplier.setVisibility(View.GONE);
            holder.editTextNumberAdditionalMultiplier.setVisibility(View.GONE);
        }
        //endregion

        //regionExpand when it is clicked
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(collapsed[position]) {
                    collapsed[position] = false;
                    holder.txtTierInfo.setVisibility(View.VISIBLE);
                    holder.productScroll.setVisibility(View.VISIBLE);
                    holder.txtMinionWarnings.setVisibility(View.VISIBLE);
                    holder.txtFuelRecLabel.setVisibility(View.VISIBLE);
                    holder.radioGroup.setVisibility(View.VISIBLE);
                    holder.txtRecFuelType.setVisibility(View.VISIBLE);
                    holder.spinnerRecUpgrade1.setVisibility(View.VISIBLE);
                    holder.spinnerRecUpgrade2.setVisibility(View.VISIBLE);
                    holder.txtUpgrade1.setVisibility(View.VISIBLE);
                    holder.txtUpgrade2.setVisibility(View.VISIBLE);
                    holder.txtPetBoostLabel.setVisibility(View.VISIBLE);
                    holder.txtExtraBoost1.setVisibility(View.VISIBLE);
                    holder.txtExtraBoost2.setVisibility(View.VISIBLE);
                    holder.editTextNumberDecimalMiscBoost1.setVisibility(View.VISIBLE);
                    holder.editTextNumberDecimalMiscBoost2.setVisibility(View.VISIBLE);
                    holder.editTextNumberRecFuel.setVisibility(View.VISIBLE);
                    holder.editTextNumberDecimalPetBoost.setVisibility(View.VISIBLE);
                    holder.btnMinionUpdate.setVisibility(View.VISIBLE);
                    holder.txtRecMultiplier.setVisibility(View.VISIBLE);
                    holder.editTextNumberAdditionalMultiplier.setVisibility(View.VISIBLE);
                    if (!solved4) {
                        holder.txtRecMultiplier.setVisibility(View.GONE);
                        holder.editTextNumberAdditionalMultiplier.setVisibility(View.GONE);
                    }
                } else {
                    collapsed[position] = true;
                    holder.txtTierInfo.setVisibility(View.GONE);
                    holder.productScroll.setVisibility(View.GONE);
                    holder.txtMinionWarnings.setVisibility(View.GONE);
                    holder.txtFuelRecLabel.setVisibility(View.GONE);
                    holder.radioGroup.setVisibility(View.GONE);
                    holder.txtRecFuelType.setVisibility(View.GONE);
                    holder.spinnerRecUpgrade1.setVisibility(View.GONE);
                    holder.spinnerRecUpgrade2.setVisibility(View.GONE);
                    holder.txtUpgrade1.setVisibility(View.GONE);
                    holder.txtUpgrade2.setVisibility(View.GONE);
                    holder.txtPetBoostLabel.setVisibility(View.GONE);
                    holder.txtExtraBoost1.setVisibility(View.GONE);
                    holder.txtExtraBoost2.setVisibility(View.GONE);
                    holder.editTextNumberDecimalMiscBoost1.setVisibility(View.GONE);
                    holder.editTextNumberDecimalMiscBoost2.setVisibility(View.GONE);
                    holder.editTextNumberRecFuel.setVisibility(View.GONE);
                    holder.editTextNumberDecimalPetBoost.setVisibility(View.GONE);
                    holder.btnMinionUpdate.setVisibility(View.GONE);
                    holder.txtRecMultiplier.setVisibility(View.GONE);
                    holder.editTextNumberAdditionalMultiplier.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            }
        });
        //endregion

        ArrayList<String> upgrades = new ArrayList<>();
        upgrades.add("Compactor");
        upgrades.add("Super Compactor");
        upgrades.add("Dwarven Super Compactor");
        upgrades.add("Auto Smelter");
        upgrades.add("Diamond Spreading");
        upgrades.add("Enchanted Egg");
        upgrades.add("Minion Expander 5%");
        upgrades.add("Flycatcher 20%");
        upgrades.add("Custom Speed Boost " + customBoostNormal + "%");
        upgrades.add("Custom Fuel Boost " + customBoostFly + "%");
        upgrades.add("Flint Shovel");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item, upgrades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerRecUpgrade1.setAdapter(adapter);
        holder.spinnerRecUpgrade2.setAdapter(adapter);
        holder.spinnerRecUpgrade1.setSelection(adapter.getPosition(minions.get(position).getUpgrade1()));
        holder.spinnerRecUpgrade2.setSelection(adapter.getPosition(minions.get(position).getUpgrade2()));
        boolean fuelType = minions.get(position).isFuelType();
        if (fuelType) {
            //If you are using percentage fuel
            holder.radioRecFuel.setChecked(true);
            holder.txtRecFuelType.setText("Fuel Boost Percentage");
        } else {
            //if you are using multiplier fuel
            holder.radioRecCatalyst.setChecked(true);
            holder.txtRecFuelType.setText("Fuel Multiplier");

        }
        holder.editTextNumberRecFuel.setText(Integer.toString(minions.get(position).getFuelNumber()));
        //endregion

        //region Make it possible to switch between fuel types for individual minions
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor editor = settings.edit();
                int catalystFuelNumber1 = settings.getInt("catalystFuelNumber",3);
                int normalFuelNumber1 = settings.getInt("normalFuelNumber",0);
                int catalystFuelNumber2 = settings.getInt(minions.get(position).getTitle() + "catalystFuelNumber",catalystFuelNumber1);
                int normalFuelNumber2 = settings.getInt(minions.get(position).getTitle() + "normalFuelNumber",normalFuelNumber1);
                String minion = minions.get(position).getTitle();
                switch (checkedId) {
                    case R.id.radioRecCatalyst:
                        editor.putInt(minion + "fuelType", 0);
                        editor.commit();
                        holder.txtRecFuelType.setText("Fuel Multiplier:");
                        holder.editTextNumberRecFuel.setText(Integer.toString(catalystFuelNumber2));
                        break;
                    case R.id.radioRecFuel:
                        editor.putInt(minion + "fuelType", 1);
                        editor.commit();
                        holder.txtRecFuelType.setText("Fuel Boost Percentage:");
                        holder.editTextNumberRecFuel.setText(Integer.toString(normalFuelNumber2));
                        break;
                }
            }
        });

        //endregion

        //region Code for saving the settings for each individual minion
        holder.btnMinionUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region First, save all the data that was collected.
                SharedPreferences.Editor editor = settings.edit();
                String minion = minions.get(position).getTitle();
                String upgrade1_1 = holder.spinnerRecUpgrade1.getSelectedItem().toString();
                String upgrade2_1 = holder.spinnerRecUpgrade2.getSelectedItem().toString();
                editor.putString(minion + "upgrade1",upgrade1_1);
                editor.putString(minion + "upgrade2",upgrade2_1);
                int defaultFuelType;
                if (minions.get(position).isFuelType()) {
                    defaultFuelType = 1;
                } else {
                    defaultFuelType = 0;
                }
                int fuelType_1 = settings.getInt(minion + "fuelType",defaultFuelType);

                int fuelNumber_1;
                if (!holder.editTextNumberRecFuel.getText().toString().equals("")) {
                    fuelNumber_1 = Integer.parseInt(holder.editTextNumberRecFuel.getText().toString());
                    if (fuelType_1 == 1) {
                        editor.putInt(minion + "normalFuelNumber", fuelNumber_1);
                    } else {
                        editor.putInt(minion + "catalystFuelNumber", fuelNumber_1);
                    }
                }

                if (!holder.editTextNumberDecimalPetBoost.getText().toString().equals("")) {
                    double petBoost = Round1(Double.parseDouble(holder.editTextNumberDecimalPetBoost.getText().toString()));
                    int petBoostStored = (int)(petBoost*10);
                    editor.putInt(minion + "petBoost",petBoostStored);
                }

                if (!holder.editTextNumberDecimalMiscBoost1.getText().toString().equals("")) {
                    String miscBoost1 = holder.editTextNumberDecimalMiscBoost1.getText().toString();
                    if (Double.parseDouble(miscBoost1) > 1000) {
                        miscBoost1 = "1000";
                    }
                    editor.putString(minion + "miscBoost1",miscBoost1);
                }

                if (!holder.editTextNumberDecimalMiscBoost2.getText().toString().equals("")) {
                    String miscBoost2 = holder.editTextNumberDecimalMiscBoost1.getText().toString();
                    if (Double.parseDouble(miscBoost2) > 1000) {
                        miscBoost2 = "1000";
                    }
                    editor.putString(minion + "miscBoost1",miscBoost2);
                }

                if (!holder.editTextNumberDecimalMiscBoost2.getText().toString().equals("")) {
                    String additionalMultiplier = holder.editTextNumberAdditionalMultiplier.getText().toString();
                    editor.putString(minion + "additionalMultiplier",additionalMultiplier);
                }

                editor.commit();
                //endregion

                //region Repeat everything from earlier
                //region Load data for this activity
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                    final String priceDataString = settings.getString("currentData",null);
                    final String defaultUpgrade1 = settings.getString("defaultUpgrade1","Compactor");
                    final String defaultUpgrade2 = settings.getString("defaultUpgrade2","Diamond Spreading");
                    final int customBoostNormal = settings.getInt("customBoostNormal",0);
                    final int customBoostFly = settings.getInt("customBoostFly",0);
                    final int catalystFuelNumber = settings.getInt("catalystFuelNumber",3);
                    final int normalFuelNumber = settings.getInt("normalFuelNumber",0);
                    final int bazaarTax = settings.getInt("personalBazaarTaxAmount",1250);
                    double bazaarTaxMultiplier = (100 - ((double)bazaarTax / 1000))/100;
                    //Load the Minion Data that I stored in the strings.xml file
                    String minionInfoString = context.getString(R.string.minionJSONData);
                    //endregion

                    //regionGet the proper JSON objects needed
                    JSONObject priceData = null;
                    JSONObject minionDataJSON = null;
                    try {
                        JSONObject priceDataBefore = new JSONObject(priceDataString);
                        priceData = priceDataBefore.getJSONObject("products");
                        minionDataJSON = new JSONObject(minionInfoString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray minionData = null;
                    try {
                        minionData = minionDataJSON.getJSONArray("minions");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //endregion

                    //regionGet usable lists of minion data
                    int minionNumber = minionData.length();
                    ArrayList<String> minionNames = new ArrayList<>();
                    ArrayList<String[]> minionProducts = new ArrayList<>();
                    ArrayList<Double[]> minionItemsPerAction = new ArrayList<>();
                    ArrayList<Double[]> minionNPCValues = new ArrayList<>();
                    ArrayList<Double[]> minionSpeeds = new ArrayList<>();
                    ArrayList<Integer> minionRelevantTiers = new ArrayList<>();
                    //Loop through the minionData JSON array and retrieve all the data we need.
                    for(int i = 0;i < minionNumber;++i){
                        try {
                            //Save lists of all the items, npc values, items per action, and speeds of all the minions
                            minionNames.add(minionData.getJSONObject(i).getString("name"));
                            JSONArray currentProducts = minionData.getJSONObject(i).getJSONArray("items");
                            JSONArray currentNPCValues = minionData.getJSONObject(i).getJSONArray("npc_val");
                            JSONArray currentItemsPerAction = minionData.getJSONObject(i).getJSONArray("items_per_action");
                            //These are all the same length
                            int numberOfItems = currentProducts.length();
                            String[] items = new String[numberOfItems];
                            Double[] NPCValues = new Double[numberOfItems];
                            Double[] itemsPerAction = new Double[numberOfItems];
                            for (int j = 0; j < numberOfItems;++j) {
                                items[j] = currentProducts.getString(j);
                                NPCValues[j] = currentNPCValues.getDouble(j);
                                itemsPerAction[j] = currentItemsPerAction.getDouble(j);
                            }
                            minionProducts.add(items);
                            minionItemsPerAction.add(itemsPerAction);
                            minionNPCValues.add(NPCValues);
                            //Minion Speeds is different so new loop is needed, its always 11, because there's 11 tiers.
                            //That was the case, but a new update changed that, so I'm fixing it now.
                            JSONObject currentMinionSpeeds = minionData.getJSONObject(i).getJSONObject("tier");
                            int speeds = currentMinionSpeeds.length();
                            minionRelevantTiers.add(speeds);
                            Double[] currentSpeeds = new Double[speeds];
                            for (int j = 1; j <= speeds;++j) {
                                String index = Integer.toString(j);
                                currentSpeeds[j-1] = currentMinionSpeeds.getDouble(index);
                            }
                            minionSpeeds.add(currentSpeeds);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //Make a place to store all this aforementioned minion data
                    ArrayList<Minion> minions = new ArrayList<>();
                    //endregion


                    //regionUse those lists to calculate profits for each minion and store all information about them in the minions array
                    for(int i = 0; i < minionNames.size(); ++i){

                        //region Display warnings if the user performs does risky stuff
                        //New warnings get appended as the user does more and more stupid things for one minion in particular
                        String warnings = "Extra Info:";
                        //endregion

                        //Figure out which upgrades we are dealing with for this Minion
                        String name = minionNames.get(i);
                        //Since we have a lot of minions, this creates and retrieves keys programmatically
                        String upgrade1 = name + "upgrade1";
                        String upgrade2 = name + "upgrade2";
                        String thisUpgrade1 = settings.getString(upgrade1,defaultUpgrade1);
                        String thisUpgrade2 = settings.getString(upgrade2,defaultUpgrade2);
                        //regionDetermine the speed of the minion
                        //Place to store the speeds
                        int desiredSize = 6;
                        if (minionRelevantTiers.get(i) > 11) {
                            desiredSize = 7;
                        }
                        Double[] timeBetweenActions = new Double[desiredSize];

                        //region Initialize all multipliers
                        int fuelType = settings.getInt(name + "fuelType",1);
                        double fuelNumber = 0;
                        int multiplierNumberInt = 1;
                        if (fuelType == 1) {
                            fuelNumber = (double)settings.getInt(name + "normalFuelNumber",normalFuelNumber);
                        } else {
                            multiplierNumberInt = settings.getInt(name + "catalystFuelNumber",catalystFuelNumber);
                        }
                        String additionalMultiplier = settings.getString(name + "additionalMultiplier","1");

                        double multiplierNumber = multiplierNumberInt * Double.parseDouble(additionalMultiplier);
                        if (Double.parseDouble(additionalMultiplier) > 1) {
                            warnings = warnings + "\nNote: You are using an extra multiplier for some reason. Congrats on completing the puzzle, and for finding whatever online trick you did that makes this necessary.";
                        }
                        int woodChecked = settings.getInt("woodChecked",0);
                        int farmChecked = settings.getInt("farmChecked",0);
                        int mithrilChecked = settings.getInt("mithrilChecked",0);
                        double petBoost = (double)settings.getInt(name+"petBoost",0)/10;
                        double miscBoost1 = Double.parseDouble(settings.getString(name + "miscBoost1","0.0"));
                        double miscBoost2 = Double.parseDouble(settings.getString(name + "miscBoost2","0.0"));

                        miscBoost1 = miscBoost1 / 100;
                        miscBoost2 = miscBoost2 / 100;

                        if (petBoost > 0) {
                            warnings = warnings + "\nNote: You are using a pet boost of " + petBoost + "%. This probably only works if you are on your island." +
                                    "\nAlso, make sure your pet actually affects this minion.";
                        }

                        petBoost = petBoost / 100;

                        double boost1 = 0;
                        if (thisUpgrade1.equals("Minion Expander 5%")) {
                            boost1 = 0.05;
                        } else if (thisUpgrade1.equals("Custom Speed Boost " + customBoostNormal +"%")) {
                            boost1 = (double)customBoostNormal / 100;
                        }

                        double boost2 = 0;
                        if (thisUpgrade2.equals("Minion Expander 5%")) {
                            boost2 = 0.05;
                        } else if (thisUpgrade2.equals("Custom Speed Boost " + customBoostNormal +"%")) {
                            boost2 = (double)customBoostNormal / 100;
                        }

                        double fuelNumberForCalculations = fuelNumber;

                        if (thisUpgrade1.equals("Flycatcher 20%") || thisUpgrade2.equals("Flycatcher 20%")) {
                            fuelNumberForCalculations = fuelNumberForCalculations + 20;
                            //Make it possible to include two flycatchers for one minion. (quite rare you'd actually do this but sure)
                            if(thisUpgrade1.equals("Flycatcher 20%") && thisUpgrade2.equals("Flycatcher 20%")) {
                                fuelNumberForCalculations += 20;
                            }
                        } else if (thisUpgrade1.equals("Custom Fuel Boost " + customBoostFly + "%") || thisUpgrade2.equals("Custom Fuel Boost " + customBoostFly + "%")) {
                            fuelNumberForCalculations = fuelNumberForCalculations + customBoostFly;
                        }

                        fuelNumberForCalculations = fuelNumberForCalculations / 100;
                        fuelNumber = fuelNumber / 100;

                        double woodBoost = 0;
                        if (woodChecked == 1) {
                            switch (name) {
                                case "Oak Minion":
                                case "Birch Minion":
                                case "Jungle Minion":
                                case "Dark Oak Minion":
                                case "Acacia Minion":
                                case "Spruce Minion":
                                    woodBoost = 0.1;
                                    warnings = warnings + "\nNote: Using Woodcutting Crystal.";
                            }
                        }
                        double farmBoost = 0;
                        if (farmChecked == 1) {
                            switch (name) {
                                case "Wheat Minion":
                                case "Carrot Minion":
                                case "Potato Minion":
                                case "Pumpkin Minion":
                                case "Melon Minion":
                                case "Mushroom Minion":
                                case "Cocoa Beans Minion":
                                case "Cactus Minion":
                                case "Sugar Cane Minion":
                                case "Nether Wart Minion":
                                    farmBoost = 0.1;
                                    warnings = warnings + "\nNote: Using Farm Crystal.";
                            }
                        }

                        double mithrilBoost = 0;
                        if(mithrilChecked == 1) {
                            switch (name) {
                                case "Cobblestone Minion":
                                case "Obsidian Minion":
                                case "Coal Minion":
                                case "Iron Minion":
                                case "Gold Minion":
                                case "Diamond Minion":
                                case "Lapis Minion":
                                case "Emerald Minion":
                                case "Redstone Minion":
                                case "Quartz Minion":
                                case "Mithril Minion":
                                    mithrilBoost = 0.1;
                                    warnings = warnings + "\nNote: Using Mithril Crystal";
                            }
                        }
                        //endregion

                        //region Apply Multipliers to each relevant tier:
                        int counterTBA = 0;
                        for (int j = 0; j < minionSpeeds.get(i).length; j+=2) {
                            Double initialTime = minionSpeeds.get(i)[j];
                            //Account for fuel
                            initialTime = RoundDownTwentieth(initialTime / (1 + fuelNumberForCalculations));
                            //Account for crystals
                            initialTime = RoundDownTwentieth(initialTime/ (1 + woodBoost));
                            initialTime = RoundDownTwentieth(initialTime/ (1 + farmBoost));
                            initialTime = RoundDownTwentieth(initialTime/ (1 + mithrilBoost));
                            //Account for Upgrades
                            initialTime = RoundDownTwentieth(initialTime / (1 + boost1));
                            initialTime = RoundDownTwentieth(initialTime / (1 + boost2));
                            //Account for Pets
                            initialTime = RoundDownTwentieth(initialTime / (1 + petBoost));
                            //Account for misc boosts
                            initialTime = RoundDownTwentieth(initialTime/ (1 + miscBoost1));
                            initialTime = RoundDownTwentieth(initialTime/ (1 + miscBoost2));

                            timeBetweenActions[counterTBA] = initialTime;
                            counterTBA += 1;
                        }

                        if(minionRelevantTiers.get(i) > 11) {
                            Double initialTime = minionSpeeds.get(i)[11];
                            //Account for fuel
                            initialTime = RoundDownTwentieth(initialTime / (1 + fuelNumberForCalculations));
                            //Account for crystals
                            initialTime = RoundDownTwentieth(initialTime/ (1 + woodBoost));
                            initialTime = RoundDownTwentieth(initialTime/ (1 + farmBoost));
                            initialTime = RoundDownTwentieth(initialTime/ (1 + mithrilBoost));
                            //Account for Upgrades
                            initialTime = RoundDownTwentieth(initialTime / (1 + boost1));
                            initialTime = RoundDownTwentieth(initialTime / (1 + boost2));
                            //Account for Pets
                            initialTime = RoundDownTwentieth(initialTime / (1 + petBoost));
                            //Account for misc boosts
                            initialTime = RoundDownTwentieth(initialTime/ (1 + miscBoost1));
                            initialTime = RoundDownTwentieth(initialTime/ (1 + miscBoost2));

                            timeBetweenActions[counterTBA] = initialTime;
                        }
                        //endregion

                        //endregion

                        //regionFigure out which products this minion produces
                        ArrayList<String> products = new ArrayList<>();
                        ArrayList<Double> npcPrices = new ArrayList<>();
                        ArrayList<Double> itemsPerAction = new ArrayList<>();
                        for (int j = 0; j < minionProducts.get(i).length; ++j) {
                            //Account for all the different products that can exist if certain upgrades are used
                            //Change should only happen on the first element
                            if (j == 0) {
                                boolean hasSmelter = (thisUpgrade1.equals("Auto Smelter") || thisUpgrade2.equals("Auto Smelter")) ||
                                        (thisUpgrade1.equals("Dwarven Super Compactor") || thisUpgrade2.equals("Dwarven Super Compactor"));
                                switch (name) {
                                    case "Cactus Minion":
                                        if (hasSmelter) {
                                            products.add("CACTUS_GREEN");
                                            npcPrices.add(1.0);
                                            itemsPerAction.add(3.0);
                                        } else {
                                            products.add("CACTUS");
                                            npcPrices.add(1.0);
                                            itemsPerAction.add(1.0);
                                            warnings = warnings + "\nWarning: No Auto Smelter used; cactus is not compactable.";
                                        }
                                        break;
                                    case "Gravel Minion":
                                        if (thisUpgrade1.equals("Flint Shovel") || thisUpgrade2.equals("Flint Shovel")) {
                                            products.add("FLINT");
                                            npcPrices.add(4.0);
                                            itemsPerAction.add(1.0);
                                        } else {
                                            products.add("GRAVEL");
                                            npcPrices.add(3.0);
                                            warnings = warnings + "\nWwarning: No Flint Shovel used; gravel is not compactable";
                                            itemsPerAction.add(1.0);
                                        }
                                        break;
                                    case "Iron Minion":
                                        if (hasSmelter) {
                                            products.add("IRON_INGOT");
                                            npcPrices.add(3.0);
                                            itemsPerAction.add(1.0);
                                        } else {
                                            products.add("IRON_ORE");
                                            npcPrices.add(3.0);
                                            itemsPerAction.add(1.0);
                                            warnings = warnings + "\nWarning: No Auto Smelter used; iron ore is not compactable";
                                        }
                                        break;
                                    case "Gold Minion":
                                        if (hasSmelter) {
                                            products.add("GOLD_INGOT");
                                            npcPrices.add(4.0);
                                            itemsPerAction.add(1.0);
                                        } else {
                                            products.add("GOLD_ORE");
                                            npcPrices.add(3.0);
                                            itemsPerAction.add(1.0);
                                            warnings = warnings + "\nWarning: No Auto Smelter used; gold ore is not compactable";
                                        }
                                        break;
                                    default:
                                        //Rest of the minions are pretty straightforward
                                        products.add(minionProducts.get(i)[j]);
                                        npcPrices.add(minionNPCValues.get(i)[j]);
                                        itemsPerAction.add(minionItemsPerAction.get(i)[j]);
                                        break;
                                }
                            } else {
                                //The weird product is the first one, the rest should be fine, for those that even produce anything
                                products.add(minionProducts.get(i)[j]);
                                npcPrices.add(minionNPCValues.get(i)[j]);
                                itemsPerAction.add(minionItemsPerAction.get(i)[j]);
                            }
                        }

                        //Fishing minions have a different multiplier than everything else
                        int timeMultiplier = 2;
                        if (name.equals("Fishing Minion")) {
                            timeMultiplier = 1;
                        }

                        //Add additional items because of diamond spreading or enchanted egg
                        if (thisUpgrade1.equals("Diamond Spreading") || thisUpgrade2.equals("Diamond Spreading")) {
                            products.add("DIAMOND");
                            npcPrices.add(8.0);
                            //Diamond spreading produces 1 extra diamond for every 10 items produced by the minion
                            itemsPerAction.add(0.1*ArraySum(minionItemsPerAction.get(i)));
                            if (thisUpgrade1.equals("Diamond Spreading") && thisUpgrade2.equals("Diamond Spreading")) {
                                warnings = warnings + "WARNING: YOU CAN'T ACTUALLY USE 2 DIAMOND SPREADINGS (the code runs assuming you picked just one, please select another upgrade)";
                            }
                        } else if (name.equals("Chicken Minion") && (thisUpgrade1.equals("Enchanted Egg") || thisUpgrade2.equals("Enchanted Egg"))) {
                            products.add("EGG");
                            npcPrices.add(3.0);
                            itemsPerAction.add(1.0);
                        }
                        //endregion

                        //region Get Enchanted Prices / Bazaar Prices
                        //Get enchanted prices for NPC
                        ArrayList<String> possibleNormalBazaarWarning = new ArrayList<>();
                        ArrayList<String> possibleEnchantedBazaarWarning = new ArrayList<>();
                        ArrayList<Double> enchantedNPCPrices = new ArrayList();
                        ArrayList<Double> bazaarNormalPrices = new ArrayList<>();
                        ArrayList<Double> bazaarEnchantedPrices = new ArrayList<>();
                        ArrayList<String> enchantedNames = new ArrayList<>();
                        Double[] enchantedDivider = new Double[npcPrices.size()];
                        for (int j = 0; j < npcPrices.size(); ++j) {
                            //Deal with enchanted prices
                            double currentPrice = npcPrices.get(j);
                            if (j == 0) {
                                switch (name) {
                                    case "Spider Minion":
                                    case "Tarantula Minion":
                                        currentPrice *= 196;
                                        enchantedDivider[j] = 196.0;
                                        break;
                                    case "Ghast Minion":
                                        currentPrice *= 5;
                                        enchantedDivider[j] = 5.0;
                                        break;
                                    case "Enderman Minion":
                                        currentPrice *= 20;
                                        enchantedDivider[j] = 20.0;
                                        break;
                                    case "Snow Minion":
                                        currentPrice *= 640;
                                        enchantedDivider[j] = 640.0;
                                        break;
                                    case "Wheat Minion":
                                        currentPrice *= 1296.0;
                                        enchantedDivider[j] = 1296.0;
                                        break;
                                    default:
                                        currentPrice *= 160;
                                        enchantedDivider[j] = 160.0;
                                }
                            } else if (j == 6) {
                                switch (name) {
                                    case "Fishing Minion":
                                        //Sponges, if you were curious
                                        currentPrice *= 40;
                                        enchantedDivider[j] = 40.0;
                                        break;
                                    default:
                                        currentPrice *= 160;
                                        enchantedDivider[j] = 160.0;
                                }
                            } else if (j == 1) {
                                switch (name) {
                                    case "Cow Minion":
                                        currentPrice *= 576;
                                        enchantedDivider[j] = 576.0;
                                        break;
                                    default:
                                        currentPrice *= 160;
                                        enchantedDivider[j] = 160.0;
                                }
                            } else if (j == 2) {
                                switch (name) {
                                    case "Rabbit Minion":
                                        currentPrice *= 576;
                                        enchantedDivider[j] = 576.0;
                                        break;
                                    default:
                                        currentPrice *= 160;
                                        enchantedDivider[j] = 160.0;
                                }
                            } else {
                                currentPrice *= 160;
                                enchantedDivider[j] = 160.0;
                            }

                            enchantedNPCPrices.add(currentPrice);
                            //Deal with normal bazaar prices
                            double bazaarNormalPrice;
                            try {
                                bazaarNormalPrice = priceData.getJSONObject(products.get(j)).getJSONArray("sell_summary").getJSONObject(0).getDouble("pricePerUnit");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                possibleNormalBazaarWarning.add("\n" + products.get(j) + " cannot be sold on the bazaar.");
                                bazaarNormalPrice = 0;
                            }
                            bazaarNormalPrices.add(bazaarNormalPrice * bazaarTaxMultiplier);

                            //Deal with enchanted bazaar prices
                            //First get the name of the enchanted item
                            String enchantedProduct = new GetEnchantedName().codeName(products.get(j));
                            enchantedNames.add(enchantedProduct);
                            double bazaarEnchantedPrice;
                            try {
                                bazaarEnchantedPrice = priceData.getJSONObject(enchantedProduct).getJSONArray("sell_summary").getJSONObject(0).getDouble("pricePerUnit");
                            } catch (JSONException e) {
                                bazaarEnchantedPrice = 0;
                                //While debugging I can find out which enchanted products I need to write exceptions for over in the GetEnchantedName class
                                Log.d("FIND_ENCHANTED",enchantedProduct);
                                possibleEnchantedBazaarWarning.add("\n" + enchantedProduct + " cannot be sold on the bazaar.");
                            }
                            bazaarEnchantedPrices.add(bazaarEnchantedPrice * bazaarTaxMultiplier);

                        }
                        //endregion

                        //regionDetermine if the items produced will be enchanted or not
                        //Determine if we need to use any of the warnings for "missing items in bazaar" from earlier
                        boolean useNormalWarning = true;
                        boolean useEnchantedWarning = true;
                        //0 for normal, 1 for enchanted, 2 for either
                        int productType = 0;
                        ArrayList<String> npcPricesString = new ArrayList<>();
                        ArrayList<String> bazaarPricesString = new ArrayList<>();
                        if(thisUpgrade1.equals("Compactor") || thisUpgrade2.equals("Compactor")) {
                            switch(name) {
                                case "Coal Minion":
                                case "Diamond Minion":
                                case "Lapis Minion":
                                case "Emerald Minion":
                                case "Gold Minion":
                                case "Iron Minion":
                                case "Redstone Minion":
                                    warnings = warnings + "\nNote: Using compactor, the blocks produced by this minion can be turned into either enchanted or normal items.";
                                    for (int j = 0; j < enchantedNames.size();++j) {
                                        products.set(j,products.get(j) + " OR " +enchantedNames.get(j));
                                        bazaarPricesString.add(addCommasAdjusted(bazaarNormalPrices.get(j)) + " OR " + addCommasAdjusted(bazaarEnchantedPrices.get(j)));
                                        npcPricesString.add(addCommasAdjusted(npcPrices.get(j)) + " OR " + addCommasAdjusted(npcPrices.get(j) * enchantedDivider[j]));
                                    }
                                    productType = 2;
                                    break;
                                case "Glowstone Minion":
                                case "Quartz Minion":
                                    warnings = warnings + "\nNote: Using compactor, the blocks produced by this minion can only be crafted into enchanted items.";
                                    for (int j = 0; j < enchantedNames.size();++j) {
                                        products.set(j,enchantedNames.get(j));
                                        bazaarPricesString.add(addCommasAdjusted(Double.toString(bazaarEnchantedPrices.get(j))));
                                        npcPricesString.add(addCommasAdjusted(Double.toString(npcPrices.get(j)*enchantedDivider[j])));
                                    }
                                    productType = 1;
                                    break;
                                case "Clay Minion":
                                    warnings = warnings + "\nNote: Using compactor, the blocks produced by this minion can only be crafted into enchanted items.";
                                    for (int j = 0; j < enchantedNames.size();++j) {
                                        if (j == 0) {
                                            products.set(0, "CLAY_BLOCK");
                                        } else {
                                            products.set(1, products.get(j) + "_BLOCK");
                                        }
                                        bazaarPricesString.add(addCommasAdjusted(Double.toString(bazaarEnchantedPrices.get(j))));
                                        npcPricesString.add(Double.toString(npcPrices.get(j)*enchantedDivider[j]));
                                    }
                                    productType = 1;
                                    break;
                                case "Snow Minion":
                                    warnings = warnings + "\nNote: Using compactor, the blocks produced by this minion can only be crafted into enchanted items.";
                                    for (int j = 0; j < enchantedNames.size();++j) {
                                        products.set(j,enchantedNames.get(j));
                                        bazaarPricesString.add(addCommasAdjusted(Double.toString(bazaarEnchantedPrices.get(j))));
                                        //Deal with snow being off
                                        if (j == 0) {
                                            npcPricesString.add("600.0");
                                        } else {
                                            npcPricesString.add(addCommasAdjusted(Double.toString(npcPrices.get(j)*enchantedDivider[j])));
                                        }
                                    }
                                    productType = 1;
                                    break;
                            }
                        }

                        if((thisUpgrade1.equals("Super Compactor") || thisUpgrade2.equals("Super Compactor")) ||
                                (thisUpgrade1.equals("Dwarven Super Compactor") || thisUpgrade2.equals("Dwarven Super Compactor"))) {
                            for (int j = 0; j < enchantedNames.size();++j) {
                                products.set(j,enchantedNames.get(j));
                                bazaarPricesString.add(addCommasAdjusted(Double.toString(bazaarEnchantedPrices.get(j))));
                                //Deal with enchanted snow prices being off
                                if(name.equals("Snow Minion") && j == 0) {
                                    npcPricesString.add("600.0");
                                } else {
                                    npcPricesString.add(addCommasAdjusted(Double.toString(npcPrices.get(j) * enchantedDivider[j])));
                                }
                                useNormalWarning = false;
                            }
                            productType = 1;
                        } else {
                            useEnchantedWarning = false;
                        }

                        //Catch some leakage
                        if (npcPricesString.size() == 0) {
                            for (int j = 0; j < enchantedNames.size(); ++j) {
                                bazaarPricesString.add(addCommasAdjusted(Double.toString(bazaarNormalPrices.get(j))));
                                npcPricesString.add(addCommasAdjusted(Double.toString(npcPrices.get(j))));
                            }
                            productType = 0;
                        }

                        //regionDeal with the mess that are wheat minions
                        if (name.equals("Wheat Minion")) {
                            if ((thisUpgrade1.equals("Super Compactor") && thisUpgrade2.equals("Compactor")) ||
                                    (thisUpgrade1.equals("Compactor") && thisUpgrade2.equals("Super Compactor"))) {
                                enchantedDivider[0] = 1296.0;
                                for (int j = 0; j < enchantedNames.size(); ++j) {
                                    products.set(j, enchantedNames.get(j));
                                    bazaarPricesString.add(addCommasAdjusted(Double.toString(bazaarEnchantedPrices.get(j))));
                                    npcPricesString.add(addCommasAdjusted(Double.toString(npcPrices.get(j))));
                                }
                                productType = 1;
                            } else {
                                warnings = warnings + "\nWARNING: ONLY VIABLE UPGRADE COMBINATION IS SUPER COMPACTOR + COMPACTOR. ANY OTHER COMBINATION AND YOUR MINION EITHER MAKES ENCHANTED BREAD (WHICH IS TRASH) OR IT FILLS UP WITH SEEDS WITHOUT MAKING ANY OTHER PRODUCTS, ITS JUST A MESS, TRUST ME OK. ALSO I TRIED TO WRITE THE CODE FOR THE REST OF THE EDGE CASES BUT IT JUST TOOK TOO LONG AND I GAVE UP. SO EITHER USE WHEAT MINION PROPERLY OR NOT AT ALL.";
                            }
                            //region Attempt to fix the rest before giving up.
    //                    else if (thisUpgrade1.equals("Compactor") || thisUpgrade2.equals("Compactor")) {
    //                        warnings = warnings + "\nUSE COMPACTOR + SUPER COMPACTOR, DO NOT SMALL BRAIN THIS, YOUR MINION WILL FILL WITH SEEDS (seriously screw you, I had to write 100 lines of code so you could have the chance to screw this up and still have it work)";
    //                        products.set(0, "HAY_BLOCK");
    //                        products.set(1, "SEEDS");
    //                        enchantedDivider[0] = 9.0;
    //                        enchantedDivider[1] = 1.0;
    //                        if (enchantedDivider.length > 2) {
    //                            enchantedDivider[2] = 9.0;
    //                        }
    //                        if (products.size() > 2) {
    //                            products.set(2, "DIAMOND_BLOCK");
    //                        }
    //                        for (int j = 0; j < enchantedNames.size(); ++j) {
    //                            if (j == 0) {
    //                                npcPricesString.add("9.0");
    //                                try {
    //                                    bazaarPricesString.add(addCommasAdjusted(Double.toString(priceData.getJSONObject("HAY_BLOCK").getJSONArray("sell_summary").getJSONObject(0).getDouble("pricePerUnit")*bazaarTaxMultiplier)));
    //                                } catch (JSONException e) {
    //                                    e.printStackTrace();
    //                                }
    //                                npcPrices.set(0, 1.0);
    //                            } else if (j == 1) {
    //                                npcPricesString.add("0.5");
    //                                try {
    //                                    bazaarPricesString.add(addCommasAdjusted(Double.toString(priceData.getJSONObject("SEEDS").getJSONArray("sell_summary").getJSONObject(0).getDouble("pricePerUnit")*bazaarTaxMultiplier)));
    //                                } catch (JSONException e) {
    //                                    e.printStackTrace();
    //                                }
    //                                npcPrices.set(1, 0.5);
    //                            } else {
    //                                npcPricesString.add("8.0 OR 1,280.0");
    //                                try {
    //                                    bazaarPricesString.add(addCommasAdjusted(priceData.getJSONObject("DIAMOND").getJSONArray("sell_summary").getJSONObject(0).getDouble("pricePerUnit")*bazaarTaxMultiplier) + " OR " +
    //                                            addCommasAdjusted(priceData.getJSONObject("ENCHANTED_DIAMOND").getJSONArray("sell_summary").getJSONObject(0).getDouble("pricePerUnit")*bazaarTaxMultiplier));
    //                                } catch (JSONException e) {
    //                                    e.printStackTrace();
    //                                }
    //                                npcPrices.set(2, 8.0);
    //                            }
    //                        }
    //                        productType = 1;
    //                    } else if (thisUpgrade1.equals("Super Compactor") || thisUpgrade2.equals("Super Compactor")) {
    //                        warnings = warnings + "\nUSE COMPACTOR + SUPER COMPACTOR, DO NOT SMALL BRAIN THIS, YOUR MINION WILL MAKE USELESS ENCHANTED BREAD (seriously screw you, I had to write 100 lines of code so you could have the chance to screw this up and still have it work)";
    //                        products.set(0, "ENCHANTED_BREAD");
    //                        products.set(1, "ENCHANTED_SEEDS");
    //                        enchantedDivider[0] = 60.0;
    //                        if (products.size() > 2) {
    //                            products.set(2, "ENCHANTED_DIAMOND");
    //                        }
    //                        for (int j = 0; j < enchantedNames.size(); ++j) {
    //                            if (j == 0) {
    //                                npcPricesString.add("60.0");
    //                                try {
    //                                    bazaarPricesString.add(addCommasAdjusted(Double.toString(priceData.getJSONObject("ENCHANTED_BREAD").getJSONArray("sell_summary").getJSONObject(0).getDouble("pricePerUnit")*bazaarTaxMultiplier)));
    //                                    bazaarEnchantedPrices.set(0,priceData.getJSONObject("ENCHANTED_BREAD").getJSONArray("sell_summary").getJSONObject(0).getDouble("pricePerUnit"));
    //                                } catch (JSONException e) {
    //                                    e.printStackTrace();
    //                                }
    //                                npcPrices.set(0, 1.0);
    //                            } else if (j == 1) {
    //                                npcPricesString.add("80.0");
    //                                try {
    //                                    bazaarPricesString.add(addCommasAdjusted(Double.toString(priceData.getJSONObject("ENCHANTED_SEEDS").getJSONArray("sell_summary").getJSONObject(0).getDouble("pricePerUnit")*bazaarTaxMultiplier)));
    //                                } catch (JSONException e) {
    //                                    e.printStackTrace();
    //                                }
    //                                npcPrices.set(1, 0.5);
    //                            } else {
    //                                npcPricesString.add("1,280.0");
    //                                try {
    //                                    bazaarPricesString.add(addCommasAdjusted(Double.toString(priceData.getJSONObject("ENCHANTED_DIAMOND").getJSONArray("sell_summary").getJSONObject(0).getDouble("pricePerUnit")*bazaarTaxMultiplier)));
    //                                } catch (JSONException e) {
    //                                    e.printStackTrace();
    //                                }
    //                                npcPrices.set(2, 8.0);
    //                            }
    //                        }
    //                        productType = 1;
    //                    }
                            //endregion
                        }
                        //endregion

                        //determine if the warnings need to be used
                        if (useNormalWarning) {
                            for (int j = 0; j < possibleNormalBazaarWarning.size(); ++j) {
                                warnings = warnings + possibleNormalBazaarWarning.get(j);
                            }
                        }

                        if (useEnchantedWarning) {
                            for (int j = 0; j< possibleEnchantedBazaarWarning.size(); ++j) {
                                warnings = warnings + possibleEnchantedBazaarWarning.get(j);
                            }
                        }
                        //endregion

                        //regionAdd all the information about this minion to the minions ArrayList
                        String tierProfits = "Profits by Tier:";
                        String productsString = "Products:";
                        String NPCPrices = "NPC Prices:";
                        String bazaarPrices = "Bazaar Prices:";
                        String itemsPerActionString = "Items Produced per Action";
                        double ranking = 0;

                        int current_tier = 1;

                        //Warn if no compactors of any kind found
                        if (productType == 0) {
                            warnings = warnings + "\nWarning: No compactors found. Minion will fill quickly, remember to empty.";
                        }

                        //region Fill out Tier info
                        for (int j = 0; j < timeBetweenActions.length;++j) {
                            //Account for Hypixel deciding to add a random tier 12 and ruining my life
                            if(current_tier > 11) {
                                current_tier = 12;
                            }
                            String currentTierInfo = "\n" + current_tier + ". Coins Per Day: ";
                            double profit = 0;
                            if (productType == 0) {
                                //For normal ones
                                for (int eachProduct = 0; eachProduct < npcPrices.size(); ++eachProduct) {
                                    double currentNPCProfit = 86400 / (timeBetweenActions[j] * timeMultiplier)
                                            * npcPrices.get(eachProduct) * itemsPerAction.get(eachProduct) * multiplierNumber;
                                    double currentBazaarProfit = 86400 / (timeBetweenActions[j] * timeMultiplier)
                                            * bazaarNormalPrices.get(eachProduct) * itemsPerAction.get(eachProduct) * multiplierNumber;
                                    if (currentBazaarProfit < currentNPCProfit) {
                                        profit += currentNPCProfit;
                                        if (j == 0) {
                                            String possibleCorrection = new FixBadNames().fix(products.get(eachProduct));
                                            if (possibleCorrection != null) {
                                                warnings = warnings + "\nWarning: Better to sell " + possibleCorrection + " to the NPC.";
                                            } else {
                                                warnings = warnings + "\nWarning: Better to sell " + products.get(eachProduct) + " to the NPC.";
                                            }
                                        }
                                    } else {
                                        profit += currentBazaarProfit;
                                    }
                                }
                            } else if (productType == 1) {
                                //For enchanted
                                for (int eachProduct = 0; eachProduct < npcPrices.size(); ++eachProduct) {
                                    //Deal with snow minions being broken
                                    double currentNPCProfit;
                                    if(name.equals("Snow Minion") && eachProduct == 0) {
                                        currentNPCProfit = 86400 /  (timeBetweenActions[j] * enchantedDivider[eachProduct] *
                                                timeMultiplier) * 600 * itemsPerAction.get(eachProduct) * multiplierNumber;
                                    } else {
                                        currentNPCProfit = 86400 /  (timeBetweenActions[j] * enchantedDivider[eachProduct] *
                                                timeMultiplier) * npcPrices.get(eachProduct) * enchantedDivider[eachProduct] * itemsPerAction.get(eachProduct) * multiplierNumber;
                                    }
                                    double currentBazaarProfit = 86400 / (timeBetweenActions[j] * enchantedDivider[eachProduct] * timeMultiplier)
                                            * bazaarEnchantedPrices.get(eachProduct) * itemsPerAction.get(eachProduct) * multiplierNumber;
                                    if (currentBazaarProfit < currentNPCProfit) {
                                        profit += currentNPCProfit;
                                        if (j == 0) {
                                            String possibleCorrection = new FixBadNames().fix(products.get(eachProduct));
                                            if (possibleCorrection != null) {
                                                warnings = warnings + "\nWarning: Better to sell " + possibleCorrection + " to the NPC.";
                                            } else {
                                                warnings = warnings + "\nWarning: Better to sell " + products.get(eachProduct) + " to the NPC.";
                                            }
                                        }
                                    } else {
                                        profit += currentBazaarProfit;
                                    }
                                }
                            } else {
                                //Deal with if there's a regular compactor

                                double eProfit = 0;

                                //For enchanted
                                for (int eachProduct = 0; eachProduct < npcPrices.size(); ++eachProduct) {
                                    //Deal with snow minions being broken
                                    double currentNPCProfit;
                                    if(name.equals("Snow Minion") && eachProduct == 0) {
                                        currentNPCProfit = 86400 /  (timeBetweenActions[j] * enchantedDivider[eachProduct] *
                                                timeMultiplier) * 600 * itemsPerAction.get(eachProduct) * multiplierNumber;
                                    } else {
                                        currentNPCProfit = 86400 /  (timeBetweenActions[j] * enchantedDivider[eachProduct] *
                                                timeMultiplier) * npcPrices.get(eachProduct) * enchantedDivider[eachProduct] * itemsPerAction.get(eachProduct) * multiplierNumber;
                                    }
                                    double currentBazaarProfit = 86400 / (timeBetweenActions[j] * enchantedDivider[eachProduct] * timeMultiplier)
                                            * bazaarEnchantedPrices.get(eachProduct) * itemsPerAction.get(eachProduct) * multiplierNumber;
                                    if (currentBazaarProfit < currentNPCProfit) {
                                        eProfit += currentNPCProfit;
                                        if (j == 0) {
                                            String possibleCorrection = new FixBadNames().fix(products.get(eachProduct));
                                            if (possibleCorrection != null) {
                                                warnings = warnings + "\nWarning: Better to sell " + possibleCorrection + " to the NPC.";
                                            } else {
                                                warnings = warnings + "\nWarning: Better to sell " + products.get(eachProduct) + " to the NPC.";
                                            }
                                        }
                                    } else {
                                        eProfit += currentBazaarProfit;
                                    }
                                }

                                double nProfit = 0;
                                //For normal ones
                                for (int eachProduct = 0; eachProduct < npcPrices.size(); ++eachProduct) {
                                    double currentNPCProfit = 86400 / (timeBetweenActions[j] * timeMultiplier)
                                            * npcPrices.get(eachProduct) * itemsPerAction.get(eachProduct) * multiplierNumber;
                                    double currentBazaarProfit = 86400 / (timeBetweenActions[j] * timeMultiplier)
                                            * bazaarNormalPrices.get(eachProduct) * itemsPerAction.get(eachProduct) * multiplierNumber;
                                    if (currentBazaarProfit < currentNPCProfit) {
                                        nProfit += currentNPCProfit;
                                        if (j == 0) {
                                            String possibleCorrection = new FixBadNames().fix(products.get(eachProduct));
                                            if (possibleCorrection != null) {
                                                warnings = warnings + "\nWarning: Better to sell " + possibleCorrection + " to the NPC.";
                                            } else {
                                                warnings = warnings + "\nWarning: Better to sell " + products.get(eachProduct) + " to the NPC.";
                                            }
                                        }
                                    } else {
                                        nProfit += currentBazaarProfit;
                                    }
                                }

                                //Determine which is better
                                if (nProfit > eProfit) {
                                    profit = nProfit;
                                    if (j == 0) {
                                        warnings = warnings + "\nNote: More profit by crafting blocks into normal items and selling versus enchanted.";
                                    }
                                } else {
                                    profit = eProfit;
                                    if (j == 0) {
                                        warnings = warnings + "\nNote: More profit by crafting blocks into enchanted items and selling versus normal.";
                                    }
                                }
                            }
                            currentTierInfo = currentTierInfo + addCommasAdjusted(Double.toString(Round1(profit)));
                            currentTierInfo = currentTierInfo + "\n     Time Between Actions: " + Round2(timeBetweenActions[j]);
                            tierProfits = tierProfits + currentTierInfo;
                            current_tier = current_tier + 2;
                            ranking += profit;
                        }
                        //endregion

                        //regionCalculate items per action
                        for (int j = 0; j < products.size(); ++j) {
                            String possibleCorrection = new FixBadNames().fix(products.get(j));
                            if (possibleCorrection != null) {
                                productsString = productsString + "\n" + possibleCorrection;
                            } else {
                                productsString = productsString + "\n" + products.get(j);
                            }
                            NPCPrices = NPCPrices + "\n" + npcPricesString.get(j);
                            bazaarPrices = bazaarPrices + "\n" + bazaarPricesString.get(j);

                            String productsMade;
                            String productsMade2 = null;
                            double productsNumber;
                            double productsNumber2 = 0;
                            if (productType == 1) {
                                productsNumber = (itemsPerAction.get(j) / timeMultiplier * multiplierNumber / enchantedDivider[j]);
                            } else if (productType == 0) {
                                productsNumber = (itemsPerAction.get(j) / timeMultiplier * multiplierNumber);
                            } else {
                                productsNumber = (itemsPerAction.get(j) / timeMultiplier * multiplierNumber);
                                productsNumber2 = (itemsPerAction.get(j) / timeMultiplier * multiplierNumber / enchantedDivider[j]);
                            }

                            if (productType == 2) {
                                NumberFormat format = new DecimalFormat("#.########");
                                productsMade = format.format(productsNumber);
                                productsMade2 = format.format(productsNumber2);

                            } else {
                                NumberFormat format = new DecimalFormat("#.########");
                                productsMade = format.format(productsNumber);
                            }

                            if (productType == 2) {
                                itemsPerActionString = itemsPerActionString + "\n" + productsMade + " OR " + productsMade2;
                            } else {
                                itemsPerActionString = itemsPerActionString + "\n" + productsMade;
                            }
                        }
                        //endregion

                        //regionFuel information
                        boolean typeOfFuel;
                        if (fuelType == 1) {
                            typeOfFuel = true;
                            fuelNumber = fuelNumber * 100;
                        } else {
                            typeOfFuel = false;
                            fuelNumber = multiplierNumberInt;
                        }
                        //endregion

                        Minion thisMinion = new Minion(name,tierProfits,productsString,itemsPerActionString,NPCPrices,
                                bazaarPrices,typeOfFuel, (int) fuelNumber,Double.parseDouble(additionalMultiplier),thisUpgrade1,thisUpgrade2,petBoost,miscBoost1,miscBoost2,warnings,ranking);
                        minions.add(thisMinion);
                        //endregion
                    }

                    //regionSort the minion list
                    //List is pretty short, doesn't really matter how efficient, so I'm just going to use a bubble sort
                    for (int a = 0; a < minions.size() - 1; ++a) {
                        for (int b = 0; b < minions.size() - a - 1; ++b) {
                            if (minions.get(b).getRankings() < minions.get(b+1).getRankings()) {
                                Collections.swap(minions,b,b+1);
                            }
                        }
                    }
                    //endregion

                    //endregion

                //endregion

                //Set the minions to minions_inside
                setMinions(minions);
                notifyDataSetChanged();
                //endregion

            }
        });
        //endregion

        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return minions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView parent;
        TextView txtMinionName;
        EditText editTextNumberRecFuel;
        Spinner spinnerRecUpgrade1;
        Spinner spinnerRecUpgrade2;
        EditText editTextNumberDecimalPetBoost;
        EditText editTextNumberAdditionalMultiplier;
        Button btnMinionUpdate;
        TextView txtMinionWarnings;
        RadioGroup radioGroup;
        RadioButton radioRecFuel;
        RadioButton radioRecCatalyst;
        TextView txtProducts;
        TextView txtBazaarPrice;
        TextView txtNPCPrice;
        TextView txtTierInfo;
        TextView txtItemsPerAction;
        TextView txtRecFuelType;
        TextView txtRecMultiplier;
        TextView txtFuelRecLabel;
        TextView txtPetBoostLabel;
        TextView txtExtraBoost1;
        TextView txtExtraBoost2;
        TextView txtUpgrade1;
        TextView txtUpgrade2;
        HorizontalScrollView productScroll;
        EditText editTextNumberDecimalMiscBoost1;
        EditText editTextNumberDecimalMiscBoost2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            txtMinionName = itemView.findViewById(R.id.txtMinionName);
            editTextNumberRecFuel = itemView.findViewById(R.id.editTextNumberRecFuel);
            spinnerRecUpgrade1 = itemView.findViewById(R.id.spinnerRecUpgrade1);
            spinnerRecUpgrade2 = itemView.findViewById(R.id.spinnerRecUpgrade2);
            editTextNumberDecimalPetBoost = itemView.findViewById(R.id.editTextNumberDecimalPetBoost);
            btnMinionUpdate = itemView.findViewById(R.id.btnMinionUpdate);
            txtMinionWarnings = itemView.findViewById(R.id.txtMinionWarnings);
            radioGroup = itemView.findViewById(R.id.radioGroupRecFuelType);
            radioRecFuel = itemView.findViewById(R.id.radioRecFuel);
            radioRecCatalyst = itemView.findViewById(R.id.radioRecCatalyst);
            txtProducts = itemView.findViewById(R.id.txtProducts);
            txtBazaarPrice = itemView.findViewById(R.id.txtBazaarPrice);
            txtNPCPrice = itemView.findViewById(R.id.txtNPCPrice);
            txtTierInfo = itemView.findViewById(R.id.txtTierInfo);
            txtItemsPerAction = itemView.findViewById(R.id.txtItemsPerAction);
            txtRecMultiplier = itemView.findViewById(R.id.txtRecMultiplier);
            txtFuelRecLabel = itemView.findViewById(R.id.txtFuelRecLabel);
            txtPetBoostLabel = itemView.findViewById(R.id.txtPetBoostLabel);
            txtExtraBoost1 = itemView.findViewById(R.id.txtExtraBoost1);
            txtExtraBoost2 = itemView.findViewById(R.id.txtExtraBoost2);
            txtUpgrade1 = itemView.findViewById(R.id.txtUpgrade1);
            txtUpgrade2 = itemView.findViewById(R.id.txtUpgrade2);
            editTextNumberDecimalMiscBoost1 = itemView.findViewById(R.id.editTextNumberDecimalMiscBoost1);
            editTextNumberDecimalMiscBoost2 = itemView.findViewById(R.id.editTextNumberDecimalMiscBoost2);
            editTextNumberAdditionalMultiplier = itemView.findViewById(R.id.editTextAdditionalMultiplier);
            productScroll = itemView.findViewById(R.id.productScroll);
            txtRecFuelType = itemView.findViewById(R.id.txtRecFuelType);
        }
    }

    public double Round1(double input) {
        BigDecimal bd = BigDecimal.valueOf(input);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    //Round to 2 decimal places
    public double Round2(double input) {
        BigDecimal bd = BigDecimal.valueOf(input);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    //Add commas to longer numbers
    public String addCommasAdjusted(String digits) {

        double placeholder = Double.parseDouble(digits);
        placeholder = Round1(placeholder);
        digits = Double.toString(placeholder);

        //Store the part with the decimal
        String afterDecimal = digits.substring(digits.length()-2);
        //Run original code on the raw string with the decimal part cut off
        String beforeDecimal = digits.substring(0,digits.length()-2);

        String result = "";
        for (int i=1; i <= beforeDecimal.length(); ++i) {
            char ch = beforeDecimal.charAt(beforeDecimal.length() - i);
            if (i % 3 == 1 && i > 1) {
                result = "," + result;
            }
            result = ch + result;
        }

        //Put the decimals back on before returning
        result = result + afterDecimal;
        return result;
    }

    //Save a step of converting, this takes a double, converts it, then uses the original method
    public String addCommasAdjusted(double number) {
        number = Round1(number);
        return addCommasAdjusted(Double.toString(number));
    }

    //Find sum of array
    public double ArraySum(Double[] array) {
        double sum = 0;
        for(double numbers:array) {
            sum += numbers;
        }
        return sum;
    }

    //Rounds down to the nearest 0.05
    public double RoundDownTwentieth(double number) {
        return Math.floor(number * 20) / 20;
    }
}
